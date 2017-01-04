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
import java.util.Locale;

import org.apache.commons.lang3.RandomStringUtils;
import org.dsu.ApplicationException;
import org.dsu.dto.ApplyLoanDTO;
import org.dsu.dto.LoanDTO;
import org.dsu.dto.PersonDTO;
import org.dsu.service.countryresolver.CountryResolverService;
import org.dsu.service.loan.LoanApplyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests apply a new loan.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebAppConfig.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class LoanControllerApplyTest {

	private static final String[] LOAN_FIELDS = { "amount", "term", "firstName", "surName" };
	private static final String[] VALID_MAXLEN_MESS = { 
			"The maximum length of the field is 1000.",
	        "The maximum length of the field is 10000.", 
	        "The maximum length of the field is 1000.",
	        "The amount value of the loan is more than maximum." };
	private static final String[] VALID_EMPTY_MESS = { "The value must not be empty.", "The value must not be empty.",
	        "The value must not be empty.", "The value must not be empty." };

	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private LoanApplyService applyLoanService;
	
	@Autowired
	private CountryResolverService countryResolverService;
	
	@Before
	public void setUp() {
		Mockito.reset(applyLoanService);
		Mockito.reset(countryResolverService);

		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void givenLongLengthAttrsLoan_WhenAskApplyLoan_ThenReturnBadRequest() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.valueOf(1000001), 
				RandomStringUtils.random(10001), 
				RandomStringUtils.random(1001),
		        RandomStringUtils.random(1001));

		ResultActions ra = postObject(dto);

		ra.andExpect(status().isBadRequest()).andExpect(content().contentType(ControllerTestUtil.APPLICATION_JSON_UTF8))
		        .andExpect(jsonPath("$.validationMessages", hasSize(4)))
		        .andExpect(jsonPath("$.validationMessages[*][0]", containsInAnyOrder(LOAN_FIELDS)))
		        .andExpect(jsonPath("$.validationMessages[*][1]", containsInAnyOrder(VALID_MAXLEN_MESS)));

		verifyZeroInteractions(applyLoanService);
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
		ra.andExpect(status().isBadRequest()).andExpect(content().contentType(ControllerTestUtil.APPLICATION_JSON_UTF8))
	        .andExpect(jsonPath("$.validationMessages", hasSize(1)))
	        .andExpect(jsonPath("$.validationMessages[0][0]", is("amount")))
	        .andExpect(jsonPath("$.validationMessages[0][1]", is("The amount value of the loan is less than minimum.")))
	        ;
		
		verifyZeroInteractions(applyLoanService);
		verifyZeroInteractions(countryResolverService);
	}
	
	@Test
	public void whenAskApplyLoan_ThenReturnLoan() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.TEN, "s", "s", "s");
		LocalDateTime createdDateTime = LocalDateTime.of(2016, 1, 1, 14, 16, 45);
		LoanDTO retLoan = new LoanDTO(1L, BigDecimal.valueOf(12.45), "Term", 
				new PersonDTO(1L, "first", "second"), createdDateTime, "approved", null);
		
		when(applyLoanService.apply(any(LoanDTO.class))).thenReturn(retLoan);

		postObject(dto)
				.andExpect(status().isOk())
				.andExpect(content().contentType(ControllerTestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1)))
	            .andExpect(jsonPath("$.amount", is(BigDecimal.valueOf(12.45).doubleValue())))
	            .andExpect(jsonPath("$.term", is("Term")))
	            .andExpect(jsonPath("$.person.id", is(1)))
	            .andExpect(jsonPath("$.person.firstName", is("first")))
	            .andExpect(jsonPath("$.person.surName", is("second")))
	            .andExpect(jsonPath("$.created", is("2016-01-01 14:16:45")))
	            .andExpect(jsonPath("$.statusName", is("approved")))
	            ;

		verify(applyLoanService, times(1)).apply(any(LoanDTO.class));
        verifyNoMoreInteractions(applyLoanService);
        verify(countryResolverService, times(1)).resolveCode();
        verifyNoMoreInteractions(countryResolverService);
	}
		
	@SuppressWarnings("unchecked")
	@Test
	public void givenApplyLoanServiceFails_WhenAskApplyLoan_ThenReturtInternalServerError() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.TEN, "s", "s", "s");
		
		when(applyLoanService.apply(any(LoanDTO.class))).thenThrow(Exception.class);
		
		postObject(dto)
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(ControllerTestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message", is("Internal server error.")))
				;
		
		verify(applyLoanService, times(1)).apply(any(LoanDTO.class));
        verifyNoMoreInteractions(applyLoanService);
        verify(countryResolverService, times(1)).resolveCode();
        verifyNoMoreInteractions(countryResolverService);
	}
	
	@Test
	public void givenPersonInBlackList_WhenAskApplyLoan_ThenReturtForbidden() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.TEN, "s", "s", "s");
		
		when(applyLoanService.apply(any(LoanDTO.class)))
			.thenThrow(new ApplicationException(ApplicationException.Type.PERSON_IN_BLACKLIST));
		
		postObject(dto)
				.andExpect(status().isForbidden())
				.andExpect(content().contentType(ControllerTestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message", is("The person is in the black list.")))
				;
		
		verify(applyLoanService, times(1)).apply(any(LoanDTO.class));
        verifyNoMoreInteractions(applyLoanService);
        verify(countryResolverService, times(1)).resolveCode();
        verifyNoMoreInteractions(countryResolverService);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void givenCountryResolverServiceFails_WhenAskApplyLoan_ThenReturtInternalServerError() throws Exception {
		ApplyLoanDTO dto = new ApplyLoanDTO(BigDecimal.TEN, "s", "s", "s");
		
		when(countryResolverService.resolveCode()).thenThrow(Exception.class);
		
		postObject(dto)
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(ControllerTestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message", is("Internal server error.")))
				;
		
		verifyZeroInteractions(applyLoanService);
        verify(countryResolverService, times(1)).resolveCode();
        verifyNoMoreInteractions(countryResolverService);
	}
	
	private ResultActions postObject(ApplyLoanDTO dto) throws Exception, JsonProcessingException {
		return mvc.perform(MockMvcRequestBuilders.post("/api/v1/loan")
				.contentType(ControllerTestUtil.APPLICATION_JSON_UTF8)
		        .locale(Locale.ENGLISH)
		        .content(objectMapper.writeValueAsBytes(dto)));
	}

	private void testEmptyInputArgsForApllyLoan(ApplyLoanDTO dto) throws Exception {

		ResultActions ra = postObject(dto);
		// System.out.println("===== " + ra.andReturn().getResponse().getContentAsString());
		ra.andExpect(status().isBadRequest()).andExpect(content().contentType(ControllerTestUtil.APPLICATION_JSON_UTF8))
		        .andExpect(jsonPath("$.validationMessages", hasSize(4)))
		        .andExpect(jsonPath("$.validationMessages[*][0]", containsInAnyOrder(LOAN_FIELDS)))
		        .andExpect(jsonPath("$.validationMessages[*][1]", containsInAnyOrder(VALID_EMPTY_MESS)));

		verifyZeroInteractions(applyLoanService);
		verifyZeroInteractions(countryResolverService);
	}

}
