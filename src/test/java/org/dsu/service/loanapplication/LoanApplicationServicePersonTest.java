/**
 * 
 */
package org.dsu.service;

import static org.dsu.TestObjectHelper.loanDto;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.dsu.dao.LoanDAO;
import org.dsu.dao.PersonDAO;
import org.dsu.domain.Loan;
import org.dsu.domain.Person;
import org.dsu.dto.LoanDTO;
import org.dsu.service.loan.LoanApplyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests {@link LoanApplyService} connected this the {@link PersonDAO} and input person's attributes.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceConfig.class })
@ActiveProfiles("test")
public class LoanApplyServicePersonTest {

	@Autowired
	private LoanDAO loanDao;

	@Autowired
	private PersonDAO personDao;

	@Autowired
	private LoanApplyService applyLoanService;

	@Before
	public void setUp() {
		Mockito.reset(loanDao);
		Mockito.reset(personDao);
	}

	@Test
	public void givenNewPerson_whenApplyLoan_ThenReturnLoan() {

		// mock the personDao.save to return a new person.
		when(personDao.save(any(Person.class))).thenReturn(new Person());

		// mock the loanDao.save to return a new loan
		Loan loan = new Loan();
		loan.setPerson(new Person());
		when(loanDao.save(any(Loan.class))).thenReturn(loan);

		applyLoanService.apply(loanDto(null, null, "firstName", "surName"));

		// verify personDao's methods save and findByFirstNameAndSurName was called once
		verify(personDao, times(1)).findByFirstNameAndSurName(eq("firstName"), eq("surName"));
		verify(personDao, times(1)).save(any(Person.class));
		verifyNoMoreInteractions(personDao);

		// verify the loanDao's method save was called once.
		verify(loanDao, times(1)).save(any(Loan.class));
		verifyNoMoreInteractions(loanDao);

	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void givenSavePersonThrowsConstraintViolation_whenApplyLoan_ThenThrowsException() {
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(null);
		when(personDao.save(any(Person.class))).thenThrow(DataIntegrityViolationException.class);

		try {
			applyLoanService.apply(loanDto(null, null, "firstName", "surName"));
		} catch (IllegalStateException e) {
			if(e.getCause().getClass() != DataIntegrityViolationException.class) {
				fail();
			}
		}
		
		verify(personDao, times(2)).findByFirstNameAndSurName(eq("firstName"), eq("surName"));
		verify(personDao, times(1)).save(any(Person.class));
		verifyNoMoreInteractions(personDao);

	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullPersonLoan_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(new LoanDTO());
	}

	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenPersonDaoSaveFail_WhenApplyLoan_ThenThrowException() {
		when(personDao.save(any(Person.class))).thenThrow(Exception.class);

		applyLoanService.apply(loanDto(null, null, "firstName", "surName"));
	}

	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenPersonDaoFindByNamesFail_WhenApplyLoan_ThenThrowException() {
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenThrow(Exception.class);

		applyLoanService.apply(loanDto(null, null, "firstName", "surName"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullPersonsFirstName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(loanDto(null, null, null, "surName"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullPersonsSurName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(loanDto(null, null, "firstName", null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenEmptyPersonsFirstName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(loanDto(null, null, "", "surName"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenEmptyPersonsSurName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(loanDto(null, null, "firstName", ""));
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenBlankPersonsFirstName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(loanDto(null, null, "  ", "surName"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenBlankPersonsSurName_whenApplyLoan_ThenThrowsException() {
		applyLoanService.apply(loanDto(null, null, "firstName", "  "));
	}
}
