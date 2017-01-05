/**
 * 
 */
package org.dsu.service;

import static org.dsu.TestObjectHelper.approvedLoan;
import static org.dsu.TestObjectHelper.country;
import static org.dsu.TestObjectHelper.loanDto;
import static org.dsu.TestObjectHelper.person;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.dsu.dao.CountryDAO;
import org.dsu.dao.LoanDAO;
import org.dsu.dao.PersonDAO;
import org.dsu.domain.Country;
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
 * Tests {@link LoanApplyService} connected this the {@link CountryDAO}.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceConfig.class })
@ActiveProfiles("test")
public class LoanApplyServiceCountryTest {

	@Autowired
	private LoanDAO loanDao;

	@Autowired
	private PersonDAO personDao;

	@Autowired
	private CountryDAO countryDao;

	@Autowired
	private LoanApplyService applyLoanService;

	@Before
	public void setUp() {
		Mockito.reset(loanDao);
		Mockito.reset(personDao);
		Mockito.reset(countryDao);
	}

	@Test
	public void givenNewCountry_whenApplyLoan_ThenReturnLoan() {
		Person person = person(1L, "firstName", "surName");
		Country country = country(1L, "ru");
		Loan loan = approvedLoan(1L, 11.6, "term", person, country);

		when(loanDao.save(any(Loan.class))).thenReturn(loan);
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(person);
		when(countryDao.findByCode(eq("ru"))).thenReturn(null);
		when(countryDao.save(any(Country.class))).thenReturn(country);

		applyLoanService.apply(loanDto(null, null, "firstName", "surName", "ru"));

		verify(countryDao, times(1)).findByCode(eq("ru"));
		verify(countryDao, times(1)).save(any(Country.class));
		verifyNoMoreInteractions(countryDao);
	}
	
	@Test
	public void givenNewCountryWithUpperCaseCode_whenApplyLoan_ThenReturnLoan() {
		Person person = person(1L, "firstName", "surName");
		Country country = country(1L, "ru");
		Loan loan = approvedLoan(1L, 11.6, "term", person, country);

		when(loanDao.save(any(Loan.class))).thenReturn(loan);
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(person);
		when(countryDao.findByCode(eq("ru"))).thenReturn(null);
		when(countryDao.save(any(Country.class))).thenReturn(country);

		applyLoanService.apply(loanDto(null, null, "firstName", "surName", "RU"));

		verify(countryDao, times(1)).findByCode(eq("ru"));
		verify(countryDao, times(1)).save(any(Country.class));
		verifyNoMoreInteractions(countryDao);
	}

	@Test
	public void givenNullCountryCode_whenApplyLoan_ThenReturnLoan() {
		Person person = person(1L, "firstName", "surName");
		Loan loan = approvedLoan(1L, 11.6, "term", person, null);

		when(loanDao.save(any(Loan.class))).thenReturn(loan);
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(person);

		LoanDTO dto = applyLoanService.apply(loanDto(null, null, "firstName", "surName", null));

		assertNull(dto.getCountryCode());

		verifyZeroInteractions(countryDao);
	}
	
	@Test
	public void givenBlankCountryCode_whenApplyLoan_ThenReturnLoan() {
		Person person = person(1L, "firstName", "surName");
		Loan loan = approvedLoan(1L, 11.6, "term", person, null);

		when(loanDao.save(any(Loan.class))).thenReturn(loan);
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(person);

		LoanDTO dto = applyLoanService.apply(loanDto(null, null, "firstName", "surName", "  "));

		assertNull(dto.getCountryCode());

		verifyZeroInteractions(countryDao);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void givenCountrySaveThrowsConstraintViolation_whenApplyLoan_ThenThrowException() {
		Person person = person(1L, "firstName", "surName");

		when(countryDao.save(any(Country.class))).thenThrow(DataIntegrityViolationException.class);
		when(countryDao.findByCode(anyString())).thenReturn(null);
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(person);
		
		try {
			applyLoanService.apply(loanDto(null, null, "firstName", "surName", "ru"));
		} catch (IllegalStateException e){
			if(e.getCause().getClass() != DataIntegrityViolationException.class) {
				fail();
			}
		}
		
		verify(countryDao, times(2)).findByCode(anyString());
		verify(countryDao, times(1)).save(any(Country.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenCountryDaoSaveFail_WhenApplyLoan_ThenThrowException() {
		when(countryDao.save(any(Country.class))).thenThrow(Exception.class);

		applyLoanService.apply(loanDto(null, null, "firstName", "surName", "ru"));
	}

	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenCountryDaoFindByNamesFail_WhenApplyLoan_ThenThrowException() {
		when(countryDao.findByCode(eq("ru"))).thenThrow(Exception.class);

		applyLoanService.apply(loanDto(null, null, "firstName", "surName", "ru"));
	}
}
