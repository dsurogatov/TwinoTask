/**
 * 
 */
package org.dsu.service.loanapplication;

import static org.dsu.TestObjectHelper.approvedLoan;
import static org.dsu.TestObjectHelper.country;
import static org.dsu.TestObjectHelper.loanDto;
import static org.dsu.TestObjectHelper.person;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.dsu.ApplicationException;
import org.dsu.dao.CountryDAO;
import org.dsu.dao.LoanDAO;
import org.dsu.dao.PersonDAO;
import org.dsu.domain.Country;
import org.dsu.domain.Loan;
import org.dsu.domain.Person;
import org.dsu.dto.LoanDTO;
import org.dsu.service.ServiceConfig;
import org.dsu.service.blacklist.PersonBlackListService;
import org.dsu.service.loan.LoanApplicationService;
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
public class LoanApplicationServiceTest {

	@Autowired
	private LoanDAO loanDao;

	@Autowired
	private PersonDAO personDao;

	@Autowired
	private CountryDAO countryDao;

	@Autowired
	private LoanApplicationService loanApplicationService;

	@Autowired
	private PersonBlackListService personBlackListService;

	@Before
	public void setUp() {
		Mockito.reset(loanDao);
		Mockito.reset(personDao);
		Mockito.reset(countryDao);
		Mockito.reset(personBlackListService);
	}

	@Test
	public void whenApplyLoan_ThenReturnLoan() {
		Person person = person(1L, "firstName", "surName");
		Country country = country(1L, "ru");
		Loan loan = approvedLoan(1L, 11.6, "term", person, country);

		// mock dao's calls
		when(loanDao.save(any(Loan.class))).thenReturn(loan);
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(person);
		when(countryDao.findByCode(eq("ru"))).thenReturn(country);

		// call
		LoanDTO dto = loanApplicationService.apply(loanDto(null, null, "firstName", "surName", "ru"));

		// check result
		assertEquals(Long.valueOf(1L), dto.getId());
		assertEquals(BigDecimal.valueOf(11.6), dto.getAmount());
		assertEquals("term", dto.getTerm());
		assertNotNull(dto.getCreated());
		assertEquals(Long.valueOf(1L), dto.getPersonDto().getId());
		assertEquals("firstName", dto.getPersonDto().getFirstName());
		assertEquals("surName", dto.getPersonDto().getSurName());
		assertEquals("approved", dto.getStatusName());
		assertEquals("ru", dto.getCountryCode());
		
		// verify calls
		verify(loanDao, times(1)).save(any(Loan.class));
		verifyNoMoreInteractions(loanDao);
		verify(personDao, times(1)).findByFirstNameAndSurName(eq("firstName"), eq("surName"));
		verifyNoMoreInteractions(personDao);
		verify(countryDao, times(1)).findByCode(eq("ru"));
		verifyNoMoreInteractions(countryDao);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenNullAppliedLoan_whenApplyLoan_ThenThrowsException() {
		loanApplicationService.apply(null);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenLoanDaoFail_WhenApplyLoan_ThenThrowException() {
		when(loanDao.save(any(Loan.class))).thenThrow(Exception.class);

		loanApplicationService.apply(loanDto(null, null, "firstName", "surName"));
	}

	@Test
	public void givenPersonInBlackList_whenApplyLoan_ThenThrowsException() {
		Person person = new Person();
		person.setId(1L);
		
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(person);
		when(personBlackListService.inList(anyString(), anyString())).thenReturn(true);

		try {
			loanApplicationService.apply(loanDto(null, null, "firstName", "surName"));
		} catch (ApplicationException e) {
			if(e.getType() != ApplicationException.Type.PERSON_IN_BLACKLIST) {
				fail();
			}
		}
		
		verify(personBlackListService, times(1)).inList(anyString(), anyString());
		verifyNoMoreInteractions(personBlackListService);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenPersonInBlackListServiceFail_WhenApplyLoan_ThenThrowException() {
		Person person = new Person();
		person.setId(1L);
		
		when(personDao.findByFirstNameAndSurName(eq("firstName"), eq("surName"))).thenReturn(person);
		when(personBlackListService.inList(anyString(), anyString())).thenThrow(Exception.class);

		loanApplicationService.apply(loanDto(null, null, "firstName", "surName"));
	}
	
}