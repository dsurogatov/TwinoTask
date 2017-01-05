/**
 * 
 */
package org.dsu.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.dsu.ApplicationException;
import org.dsu.dto.ApplyLoanDTO;
import org.dsu.dto.LoanDTO;
import org.dsu.dto.PersonDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Tests apply a new loan.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebAppConfig.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class LoanControllerApplyTest extends BaseLoanControllerApplyTest {

	private static final String[] LOAN_FIELDS = { "amount", "term", "firstName", "surName" };
	private static final String[] VALID_MAXLEN_MESS = { 
			"The maximum length of the field is 1000.",
	        "The maximum length of the field is 10000.", 
	        "The maximum length of the field is 1000.",
	        "The amount value of the loan is more than maximum." };
	private static final String[] VALID_EMPTY_MESS = { "The value must not be empty.", "The value must not be empty.",
	        "The value must not be empty.", "The value must not be empty." };

	@Test
	public void givenLongLengthAttrsLoan_WhenAskApplyLoan_ThenReturnBadRequest() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.valueOf(1000001), 
				RandomStringUtils.random(10001), 
				RandomStringUtils.random(1001),
		        RandomStringUtils.random(1001));

		ResultActions ra = postObject(dto);

		ra.andExpect(status().isBadRequest()).andExpect(content().contentType(TestControllerUtil.APPLICATION_JSON_UTF8))
		        .andExpect(jsonPath("$.validationMessages", hasSize(4)))
		        .andExpect(jsonPath("$.validationMessages[*][0]", containsInAnyOrder(LOAN_FIELDS)))
		        .andExpect(jsonPath("$.validationMessages[*][1]", containsInAnyOrder(VALID_MAXLEN_MESS)));

		verifyZeroInteractions(loanApplicationService);
		verifyZeroInteractions(countryResolverService);
	}

	@Test
	public void givenNullAttrsLoan_WhenAskApplyLoan_ThenReturnBadRequest() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(null, null, null, null);
		testEmptyInputArgsForApllyLoan(dto);
	}

	@Test
	public void givenEmptyStringAttrsLoan_WhenAskApplyLoan_ThenReturnBadRequest() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(null, "", "", "");
		testEmptyInputArgsForApllyLoan(dto);
	}

	@Test
	public void givenBlankStringAttrsLoan_WhenAskApplyLoan_ThenReturnBadRequest() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(null, " ", " ", " ");
		testEmptyInputArgsForApllyLoan(dto);
	}

	@Test
	public void givenMinAmount_WhenAskApplyLoan_ThenReturnBadRequest() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.ZERO, "s", "s", "s");

		ResultActions ra = postObject(dto);
		ra.andExpect(status().isBadRequest()).andExpect(content().contentType(TestControllerUtil.APPLICATION_JSON_UTF8))
	        .andExpect(jsonPath("$.validationMessages", hasSize(1)))
	        .andExpect(jsonPath("$.validationMessages[0][0]", is("amount")))
	        .andExpect(jsonPath("$.validationMessages[0][1]", is("The amount value of the loan is less than minimum.")))
	        ;
		
		verifyZeroInteractions(loanApplicationService);
		verifyZeroInteractions(countryResolverService);
	}
	
	@Test
	public void whenAskApplyLoan_ThenReturnLoan() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.TEN, "s", "s", "s");
		LocalDateTime createdDateTime = LocalDateTime.of(2016, 1, 1, 14, 16, 45);
		LoanDTO retLoan = new LoanDTO(1L, BigDecimal.valueOf(12.45), "Term", 
				new PersonDTO(1L, "first", "second"), createdDateTime, "approved", null);
		
		when(loanApplicationService.apply(any(LoanDTO.class))).thenReturn(retLoan);
		when(countryResolverService.resolveCode()).thenReturn("ru");
		
		postObject(dto)
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestControllerUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1)))
	            .andExpect(jsonPath("$.amount", is(BigDecimal.valueOf(12.45).doubleValue())))
	            .andExpect(jsonPath("$.term", is("Term")))
	            .andExpect(jsonPath("$.person.id", is(1)))
	            .andExpect(jsonPath("$.person.firstName", is("first")))
	            .andExpect(jsonPath("$.person.surName", is("second")))
	            .andExpect(jsonPath("$.created", is("2016-01-01 14:16:45")))
	            .andExpect(jsonPath("$.statusName", is("approved")))
	            ;

		verify(loanApplicationService, times(1)).apply(any(LoanDTO.class));
        verifyNoMoreInteractions(loanApplicationService);
        verify(countryResolverService, times(1)).resolveCode();
        verifyNoMoreInteractions(countryResolverService);
	}
		
	@SuppressWarnings("unchecked")
	@Test
	public void givenApplyLoanServiceFails_WhenAskApplyLoan_ThenReturnInternalServerError() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.TEN, "s", "s", "s");
		
		when(countryResolverService.resolveCode()).thenReturn("ru");
		when(loanApplicationService.apply(any(LoanDTO.class))).thenThrow(Exception.class);
		
		postObject(dto)
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(TestControllerUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message", is("Internal server error.")))
				;
		
		verify(loanApplicationService, times(1)).apply(any(LoanDTO.class));
        verifyNoMoreInteractions(loanApplicationService);
        verify(countryResolverService, times(1)).resolveCode();
        verifyNoMoreInteractions(countryResolverService);
	}
	
	@Test
	public void givenPersonInBlackList_WhenAskApplyLoan_ThenReturnForbidden() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.TEN, "s", "s", "s");
		
		when(countryResolverService.resolveCode()).thenReturn("ru");
		when(loanApplicationService.apply(any(LoanDTO.class)))
			.thenThrow(new ApplicationException(ApplicationException.Type.PERSON_IN_BLACKLIST));
		
		postObject(dto)
				.andExpect(status().isForbidden())
				.andExpect(content().contentType(TestControllerUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message", is("The person is in the black list.")))
				;
		
		verify(loanApplicationService, times(1)).apply(any(LoanDTO.class));
        verifyNoMoreInteractions(loanApplicationService);
        verify(countryResolverService, times(1)).resolveCode();
        verifyNoMoreInteractions(countryResolverService);
	}

	private void testEmptyInputArgsForApllyLoan(ApplyLoanDTO dto) throws Exception {

		ResultActions ra = postObject(dto);
		// System.out.println("===== " + ra.andReturn().getResponse().getContentAsString());
		ra.andExpect(status().isBadRequest()).andExpect(content().contentType(TestControllerUtil.APPLICATION_JSON_UTF8))
		        .andExpect(jsonPath("$.validationMessages", hasSize(4)))
		        .andExpect(jsonPath("$.validationMessages[*][0]", containsInAnyOrder(LOAN_FIELDS)))
		        .andExpect(jsonPath("$.validationMessages[*][1]", containsInAnyOrder(VALID_EMPTY_MESS)));

		verifyZeroInteractions(loanApplicationService);
		verifyZeroInteractions(countryResolverService);
	}

}
