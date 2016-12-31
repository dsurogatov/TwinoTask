package org.dsu.service;

import static org.dsu.TestObjectHelper.PAGE_DEFAULT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.dsu.Page;
import org.dsu.dao.LoanDAO;
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
	private LoanService loanService;
	
	@Before
	public void setUp() {
		Mockito.reset(loanDao);
	}
	
	@Test
	public void whenRunFindAllApproved_ThenReturnLoanList() {
		Loan loan = new Loan();
		loan.setId(1L);
		loan.setAmount(BigDecimal.valueOf(11.6));
		loan.setTerm("term");
		loan.setStatus(LoanStatus.APPROVED);
		Person person = new Person();
		person.setId(1L);
		person.setFirstName("firstName");
		person.setSurName("surName");
		loan.setPerson(person);
		List<Loan> list = new ArrayList<>();
		list.add(loan);
		
		// mock dao to find loans
		when(loanDao.findByStatus(eq(LoanStatus.APPROVED), any(Pageable.class))).thenReturn(list);
	
		List<LoanDTO> result = loanService.findAllApproved(PAGE_DEFAULT);
		
		assertEquals(1, result.size());
		LoanDTO dto = result.get(0);
		assertEquals(Long.valueOf(1L), dto.getId());
		assertEquals(BigDecimal.valueOf(11.6), dto.getAmount());
		assertEquals("term", dto.getTerm());
		assertEquals(loan.getCreated(), dto.getCreated());
		assertEquals(Long.valueOf(1L), dto.getPersonDto().getId());
		assertEquals("firstName", dto.getPersonDto().getFirstName());
		assertEquals("surName", dto.getPersonDto().getSurName());

		// verify number of calls 
		verify(loanDao, times(1)).findByStatus(eq(LoanStatus.APPROVED), any(Pageable.class));
        verifyNoMoreInteractions(loanDao);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenNullPage_WhenFindAllApproved_ThenThrowException() {
		loanService.findAllApproved(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenPageWithBigPageSize_WhenFindAllApproved_ThenThrowException() {
		loanService.findAllApproved(new Page(0, 2001, null));
	}
}
