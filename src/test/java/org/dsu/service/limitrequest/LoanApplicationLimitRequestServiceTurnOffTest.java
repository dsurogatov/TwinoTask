/**
 * 
 */
package org.dsu.service.limitrequest;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.dsu.dao.LoanDAO;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Tests the {@link LoanApplicationLimitRequestServiceImpl}
 * then a validation is turned off.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceConfig.class })
@ActiveProfiles("test")
public class LoanApplicationLimitRequestServiceTurnOffTest {

	@Autowired
	private LoanApplicationLimitRequestService requestService;
	
	@Autowired
	private LoanDAO loanDao;
	
	@Before
	public void setUp() {
		Mockito.reset(loanDao);
	}
	
	@Test
	public void whenRunRequestReached_ThenReturnTrue() {
		boolean result = requestService.limitReachedByCountry("ru");
		assertFalse(result);

		verifyZeroInteractions(loanDao);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenBlankCountryCode_whenRunRequestReached_ThenThrowException() {
		requestService.limitReachedByCountry("   ");
	}
}
