/**
 * 
 */
package org.dsu.service;

import static org.dsu.TestObjectHelper.approvedLoan;
import static org.dsu.TestObjectHelper.person;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.dsu.ApplicationException;
import org.dsu.dao.LoanDAO;
import org.dsu.dao.PersonDAO;
import org.dsu.domain.Loan;
import org.dsu.domain.Person;
import org.dsu.dto.ApplyLoanDTO;
import org.dsu.dto.LoanDTO;
import org.dsu.service.blacklist.PersonBlackListService;
import org.dsu.service.loan.ApplyLoanService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the ApplyLoanService.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceConfig.class })
@ActiveProfiles("test")
public class ApplyLoanServiceTest {

	@Autowired
	private LoanDAO loanDao;

	@Autowired
	private PersonDAO personDao;

	@Autowired
	private ApplyLoanService applyLoanService;

	@Autowired
	private PersonBlackListService personBlackListService;

	@Before
	public void setUp() {
		Mockito.reset(loanDao);
		Mockito.reset(personDao);
		Mockito.reset(personBlackListService);
	}

	@Test
	public void whenApplyLoan_ThenReturnLoan() {
		Person person = person(1L, "firstName", "surName");
		Loan loan = approvedLoan(1L, 11.6, "term", person);

		// mock dao's calls
		when(loanDao.save(any(Loan.class))).thenReturn(loan);
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(person);

		// call
		LoanDTO dto = applyLoanService.apply(new ApplyLoanDTO(null, null, "firstName", "surName"));

		// check result
		assertEquals(Long.valueOf(1L), dto.getId());
		assertEquals(BigDecimal.valueOf(11.6), dto.getAmount());
		assertEquals("term", dto.getTerm());
		assertNotNull(dto.getCreated());
		assertEquals(Long.valueOf(1L), dto.getPersonDto().getId());
		assertEquals("firstName", dto.getPersonDto().getFirstName());
		assertEquals("surName", dto.getPersonDto().getSurName());
		assertEquals("approved", dto.getStatusName());
		
		// verify calls
		verify(loanDao, times(1)).save(any(Loan.class));
		verifyNoMoreInteractions(loanDao);
		verify(personDao, times(1)).findByFirstNameAndSurName(eq("firstName"), eq("surName"));
		verifyNoMoreInteractions(personDao);
	}
	
	@Test
	public void givenNewPerson_whenApplyLoan_ThenReturnLoan() {
		
		// mock the personDao.save to return a new person.
		when(personDao.save(any(Person.class))).thenReturn(new Person());
		
		// mock the loanDao.save to return a new loan
		Loan loan = new Loan();
		loan.setPerson(new Person());
		when(loanDao.save(any(Loan.class))).thenReturn(loan);
		
		applyLoanService.apply(new ApplyLoanDTO(null, null, "firstName", "surName"));
		
		// verify personDao's methods save and findByFirstNameAndSurName was called once
		verify(personDao, times(1)).findByFirstNameAndSurName(eq("firstName"), eq("surName"));
		verify(personDao, times(1)).save(any(Person.class));
		verifyNoMoreInteractions(personDao);
		
		// verify the loanDao's method save was called once.
		verify(loanDao, times(1)).save(any(Loan.class));
		verifyNoMoreInteractions(loanDao);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenNullAppliedLoan_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(null);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenLoanDaoFail_WhenApplyLoan_ThenThrowException() {
		when(loanDao.save(any(Loan.class))).thenThrow(Exception.class);

		applyLoanService.apply(new ApplyLoanDTO(null, null, "firstName", "surName"));
	}

	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenPersonDaoSaveFail_WhenApplyLoan_ThenThrowException() {
		when(personDao.save(any(Person.class))).thenThrow(Exception.class);

		applyLoanService.apply(new ApplyLoanDTO(null, null, "firstName", "surName"));
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenPersonDaoFindByNamesFail_WhenApplyLoan_ThenThrowException() {
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenThrow(Exception.class);

		applyLoanService.apply(new ApplyLoanDTO(null, null, "firstName", "surName"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullPersonsFirstName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(new ApplyLoanDTO(null, null, null, "surName"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenNullPersonsSurName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(new ApplyLoanDTO(null, null, "firstName", null));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenEmptyPersonsFirstName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(new ApplyLoanDTO(null, null, "", "surName"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenEmptyPersonsSurName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(new ApplyLoanDTO(null, null, "firstName", ""));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenBlankPersonsFirstName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(new ApplyLoanDTO(null, null, "  ", "surName"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenBlankPersonsSurName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(new ApplyLoanDTO(null, null, "firstName", "  "));
	}
	
	@Test
	public void givenPersonInBlackList_whenApplyLoan_ThenThrowsException() {
		Person person = new Person();
		person.setId(1L);
		
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(person);
		when(personBlackListService.inList(anyLong())).thenReturn(true);

		try {
			applyLoanService.apply(new ApplyLoanDTO(null, null, "firstName", "surName"));
		} catch (ApplicationException e) {
			if(e.getType() != ApplicationException.Type.PERSON_IN_BLACKLIST) {
				fail();
			}
		}
		
		verify(personBlackListService, times(1)).inList(anyLong());
		verifyNoMoreInteractions(personBlackListService);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenPersonInBlackListServiceFail_WhenApplyLoan_ThenThrowException() {
		Person person = new Person();
		person.setId(1L);
		
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(person);
		when(personBlackListService.inList(anyLong())).thenThrow(Exception.class);

		applyLoanService.apply(new ApplyLoanDTO(null, null, "firstName", "surName"));
	}
	
}