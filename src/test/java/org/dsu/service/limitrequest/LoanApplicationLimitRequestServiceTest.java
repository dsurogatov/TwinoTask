/**
 * 
 */
package org.dsu.service.limitrequest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.dsu.TestObjectHelper;
import org.dsu.dao.LoanDAO;
import org.dsu.domain.Country;
import org.dsu.domain.Loan;
import org.dsu.domain.Person;
import org.dsu.service.ServiceConfig;
import org.dsu.service.validation.LoanApplicationLimitRequestService;
import org.dsu.service.validation.LoanApplicationLimitRequestServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Tests the {@link LoanApplicationLimitRequestServiceImpl}.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceConfig.class })
@ActiveProfiles("test")
@TestPropertySource(properties = {
		"LoanApplicationLimitRequest.validate.second = true",
		"LoanApplicationLimitRequest.validate.too.many = true",
		"LoanApplicationLimitRequest.too.many.timeframe.sec = 10",
		"LoanApplicationLimitRequest.too.many.count = 2"
})
public class LoanApplicationLimitRequestServiceTest {

	@Autowired
	private LoanApplicationLimitRequestService requestService;
	
	@Autowired
	private LoanDAO loanDao;
	
	@Before
	public void setUp() {
		Mockito.reset(loanDao);
	}
	
	@Test
	public void givenPrevLoanFromSameCountry_WhenRunLimitReached_ThenReturnTrue() {
		Country country = new Country();
		country.setCode("ru");
		Loan loan = TestObjectHelper.approvedLoan(1L, 1, "term", new Person(), country);
		when(loanDao.findTopByOrderByCreatedDesc()).thenReturn(loan);
		
		boolean result = requestService.limitReachedByCountry("ru");
		assertTrue(result);
		
		verify(loanDao).findTopByOrderByCreatedDesc();
		verifyNoMoreInteractions(loanDao);
	}
	
	@Test
	public void givenPrevLoanFromSameCountry_WhenRunLimitReachedWithCapitalizeCode_ThenReturnTrue() {
		Country country = new Country();
		country.setCode("ru");
		Loan loan = TestObjectHelper.approvedLoan(1L, 1, "term", new Person(), country);
		when(loanDao.findTopByOrderByCreatedDesc()).thenReturn(loan);
		
		boolean result = requestService.limitReachedByCountry("RU");
		assertTrue(result);
		
		verify(loanDao).findTopByOrderByCreatedDesc();
		verifyNoMoreInteractions(loanDao);
	}
	
	@Test
	public void givenTooManyRequest_WhenRunLimitReached_ThenReturnTrue() {
		
		when(loanDao.findTopByOrderByCreatedDesc()).thenReturn(null);
		when(loanDao.countByCountryCodeAndCreatedBetween(anyString(), 
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(2);
		
		boolean result = requestService.limitReachedByCountry("ru");
		assertTrue(result);
		
		verify(loanDao).findTopByOrderByCreatedDesc();
		verify(loanDao).countByCountryCodeAndCreatedBetween(anyString(), 
				any(LocalDateTime.class), any(LocalDateTime.class));
		verifyNoMoreInteractions(loanDao);
	}
	
	@Test
	public void givenTooManyRequest_WhenRunLimitReachedWithCapitalizeCode_ThenReturnTrue() {
		
		when(loanDao.findTopByOrderByCreatedDesc()).thenReturn(null);
		when(loanDao.countByCountryCodeAndCreatedBetween(eq("ru"), 
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(2);
		
		boolean result = requestService.limitReachedByCountry("RU");
		assertTrue(result);
		
		verify(loanDao).findTopByOrderByCreatedDesc();
		verify(loanDao).countByCountryCodeAndCreatedBetween(eq("ru"), 
				any(LocalDateTime.class), any(LocalDateTime.class));
		verifyNoMoreInteractions(loanDao);
	}
	
	@Test
	public void givenPrevLoanNull_WhenRunLimitReached_ThenReturnFalse() {
		when(loanDao.findTopByOrderByCreatedDesc()).thenReturn(null);
		
		boolean result = requestService.limitReachedByCountry("ru");
		assertFalse(result);
		
		verify(loanDao).findTopByOrderByCreatedDesc();
	}
	
	@Test
	public void givenPrevLoanFromOtherCountry_WhenRunLimitReached_ThenReturnFalse() {
		Country country = new Country();
		country.setCode("ru");
		Loan loan = TestObjectHelper.approvedLoan(1L, 1, "term", new Person(), country);
		when(loanDao.findTopByOrderByCreatedDesc()).thenReturn(loan);
		
		boolean result = requestService.limitReachedByCountry("us");
		assertFalse(result);
		
		verify(loanDao).findTopByOrderByCreatedDesc();
	}
	
	@Test
	public void givenNotManyRequest_WhenRunLimitReached_ThenReturnFalse() {
		
		when(loanDao.findTopByOrderByCreatedDesc()).thenReturn(null);
		when(loanDao.countByCountryCodeAndCreatedBetween(anyString(), 
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(1);
		
		boolean result = requestService.limitReachedByCountry("ru");
		assertFalse(result);
		
		verify(loanDao).findTopByOrderByCreatedDesc();
		verify(loanDao).countByCountryCodeAndCreatedBetween(anyString(), 
				any(LocalDateTime.class), any(LocalDateTime.class));
		verifyNoMoreInteractions(loanDao);
	}
	
	// test fail daos
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenValidationSecondThrowException_WhenRunLimitReached_ThenReturnFalse() {
		when(loanDao.findTopByOrderByCreatedDesc()).thenThrow(Exception.class);
		
		requestService.limitReachedByCountry("ru");
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenValidationTooManyThrowException_WhenRunLimitReached_ThenReturnFalse() {
		when(loanDao.countByCountryCodeAndCreatedBetween(anyString(), 
				any(LocalDateTime.class), any(LocalDateTime.class))).thenThrow(Exception.class);
		
		requestService.limitReachedByCountry("ru");
	}
}
