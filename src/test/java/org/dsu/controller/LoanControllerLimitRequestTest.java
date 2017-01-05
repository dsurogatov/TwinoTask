/**
 * 
 */
package org.dsu.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.dsu.dto.ApplyLoanDTO;
import org.dsu.service.validation.LoanApplicationLimitRequestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/** Tests the {@link LoanController#applyLoan(org.dsu.dto.ApplyLoanDTO)} 
 * by validating number of requests from the same country.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebAppConfig.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class LoanControllerLimitRequestTest extends BaseLoanControllerApplyTest {
	
	@Autowired
	private LoanApplicationLimitRequestService limitRequestService;
	
	@Before
	public void setUpLimitRequest() {
		Mockito.reset(limitRequestService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void givenCountryResolverServiceFails_WhenAskApplyLoan_ThenReturnInternalServerError() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.TEN, "s", "s", "s");
		
		when(countryResolverService.resolveCode()).thenThrow(Exception.class);
		
		postObject(dto)
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(TestControllerUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message", is("Internal server error.")))
				;
		
		verifyZeroInteractions(loanApplicationService);
        verify(countryResolverService, times(1)).resolveCode();
        verifyNoMoreInteractions(countryResolverService);
	}
	
	@Test
	public void givenCountryResolverServiceReturnBlankCode_WhenAskApplyLoan_ThenReturtInternalServerError() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.TEN, "s", "s", "s");
		
		when(countryResolverService.resolveCode()).thenReturn(null);
		
		postObject(dto)
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(TestControllerUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message", is("Internal server error.")))
				;
		
		verifyZeroInteractions(loanApplicationService);
        verify(countryResolverService, times(1)).resolveCode();
        verifyNoMoreInteractions(countryResolverService);
	}
	
	@Test
	public void givenLimitRequestReached_WhenAskApplyLoan_ThenReturnForbidden() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.TEN, "s", "s", "s");
		
		when(countryResolverService.resolveCode()).thenReturn("ru");
		when(limitRequestService.limitReachedByCountry(anyString())).thenReturn(true);
		
		postObject(dto)
				.andExpect(status().isForbidden())
				.andExpect(content().contentType(TestControllerUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message", is("The number of loan applications coming from the country is reached.")))
				;
		
		verifyZeroInteractions(loanApplicationService);
        verify(countryResolverService, times(1)).resolveCode();
        verifyNoMoreInteractions(countryResolverService);
        verify(limitRequestService, times(1)).limitReachedByCountry(anyString());
        verifyNoMoreInteractions(limitRequestService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void givenLimitRequestServiceFails_WhenAskApplyLoan_ThenReturnInternalServerError() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.TEN, "s", "s", "s");
		
		when(countryResolverService.resolveCode()).thenReturn("ru");
		when(limitRequestService.limitReachedByCountry(anyString())).thenThrow(Exception.class);
		
		postObject(dto)
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(TestControllerUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message", is("Internal server error.")))
				;
		
		verifyZeroInteractions(loanApplicationService);
        verify(countryResolverService, times(1)).resolveCode();
        verifyNoMoreInteractions(countryResolverService);
        verify(limitRequestService, times(1)).limitReachedByCountry(anyString());
        verifyNoMoreInteractions(limitRequestService);
	}
}
