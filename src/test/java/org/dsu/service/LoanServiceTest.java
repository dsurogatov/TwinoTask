package org.dsu.service;

import static org.dsu.TestObjectHelper.PAGE_DEFAULT;
import static org.dsu.TestObjectHelper.approvedLoan;
import static org.dsu.TestObjectHelper.person;
import static org.dsu.TestObjectHelper.country;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.dsu.Page;
import org.dsu.dao.LoanDAO;
import org.dsu.dao.PersonDAO;
import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.dsu.domain.Person;
import org.dsu.dto.LoanDTO;
import org.dsu.service.loan.LoanService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceConfig.class })
@ActiveProfiles("test")
public class LoanServiceTest {

	@Autowired
	private LoanDAO loanDao;

	@Autowired
	private PersonDAO personDao;

	@Autowired
	private LoanService loanService;

	@Before
	public void setUp() {
		Mockito.reset(loanDao);
		Mockito.reset(personDao);
	}

	@Test
	public void whenRunFindApprovedLoans_ThenReturnLoanList() {
		Loan loan = approvedLoan(1L, 11.6, "term", person(1L, "firstName", "surName"),  country(1L, "ru"));
		List<Loan> list = Arrays.asList(loan);

		// mock dao to find loans
		when(loanDao.findByStatus(eq(LoanStatus.APPROVED), any(Pageable.class))).thenReturn(list);

		List<LoanDTO> result = loanService.findApprovedLoans(PAGE_DEFAULT);

		assertEquals(1, result.size());
		LoanDTO dto = result.get(0);
		assertEquals(Long.valueOf(1L), dto.getId());
		assertEquals(BigDecimal.valueOf(11.6), dto.getAmount());
		assertEquals("term", dto.getTerm());
		assertEquals(loan.getCreated(), dto.getCreated());
		assertEquals(Long.valueOf(1L), dto.getPersonDto().getId());
		assertEquals("firstName", dto.getPersonDto().getFirstName());
		assertEquals("surName", dto.getPersonDto().getSurName());
		assertEquals("ru", dto.getCountryCode());

		// verify number of calls
		verify(loanDao, times(1)).findByStatus(eq(LoanStatus.APPROVED), any(Pageable.class));
		verifyNoMoreInteractions(loanDao);
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullPage_WhenFindApprovedLoans_ThenThrowException() {
		loanService.findApprovedLoans(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenPageWithBigPageSize_WhenFindApprovedLoans_ThenThrowException() {
		loanService.findApprovedLoans(new Page(0, 2001, null));
	}

	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenLoanDaoFail_WhenFindApprovedLoans_ThenThrowException() {
		when(loanDao.findByStatus(eq(LoanStatus.APPROVED), any(Pageable.class))).thenThrow(Exception.class);

		loanService.findApprovedLoans(PAGE_DEFAULT);
	}

	@Test
	public void whenRunFindApprovedLoansByPersonId_ThenReturnLoanList() {
		Loan loan = approvedLoan(1L, 11.6, "term", person(1L, "firstName", "surName"), country(1L, "ru"));
		List<Loan> list = Arrays.asList(loan);

		when(loanDao.findByStatusAndPerson(eq(LoanStatus.APPROVED), any(Person.class), any(Pageable.class)))
		        .thenReturn(list);
		when(personDao.findOne(anyLong())).thenReturn(new Person());

		List<LoanDTO> result = loanService.findApprovedLoansByPersonId(1L, PAGE_DEFAULT);

		assertEquals(1, result.size());
		LoanDTO dto = result.get(0);
		assertEquals(Long.valueOf(1L), dto.getId());
		assertEquals(BigDecimal.valueOf(11.6), dto.getAmount());
		assertEquals("term", dto.getTerm());
		assertEquals(loan.getCreated(), dto.getCreated());
		assertEquals(Long.valueOf(1L), dto.getPersonDto().getId());
		assertEquals("firstName", dto.getPersonDto().getFirstName());
		assertEquals("surName", dto.getPersonDto().getSurName());
		assertEquals("ru", dto.getCountryCode());

		verify(loanDao, times(1)).findByStatusAndPerson(eq(LoanStatus.APPROVED), any(Person.class),
		        any(Pageable.class));
		verifyNoMoreInteractions(loanDao);
		verify(personDao, times(1)).findOne(anyLong());
		verifyNoMoreInteractions(personDao);
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullPage_WhenFindApprovedLoansByPersonId_ThenThrowException() {
		loanService.findApprovedLoansByPersonId(1L, null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void givenPageWithBigPageSize_WhenFindApprovedLoansByPersonId_ThenThrowException() {
		loanService.findApprovedLoansByPersonId(1L, new Page(0, 2001, null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullPersonId_WhenFindApprovedLoansByPersonId_ThenThrowException() {
		loanService.findApprovedLoansByPersonId(null, PAGE_DEFAULT);
	}

	@Test
	public void givenNotExistPerson_WhenFindApprovedLoansByPersonId_ThenReturnEmptyList() {
		List<LoanDTO> result = loanService.findApprovedLoansByPersonId(1L, PAGE_DEFAULT);

		assertTrue(result.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenLoanDaoFail_WhenFindApprovedLoansByPersonId_ThenThrowException() {
		when(loanDao.findByStatusAndPerson(eq(LoanStatus.APPROVED), any(Person.class), any(Pageable.class)))
		        .thenThrow(Exception.class);
		when(personDao.findOne(anyLong())).thenReturn(new Person());

		loanService.findApprovedLoansByPersonId(1L, PAGE_DEFAULT);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenPersonDaoFail_WhenFindApprovedLoansByPersonId_ThenThrowException() {
		when(personDao.findOne(anyLong())).thenThrow(Exception.class);

		loanService.findApprovedLoansByPersonId(1L, PAGE_DEFAULT);
	}
}
