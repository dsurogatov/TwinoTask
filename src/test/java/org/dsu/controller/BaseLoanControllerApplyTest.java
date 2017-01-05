/**
 * 
 */
package org.dsu.controller;

import java.util.Locale;

import org.dsu.dto.ApplyLoanDTO;
import org.dsu.service.countryresolver.CountryResolverService;
import org.dsu.service.loan.LoanApplyService;
import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/** The base class for LoanControllerApplyTest.
 * It contains some helper methods.
 *
 * @author nescafe
 */
public class BaseLoanControllerApplyTest {

	protected MockMvc mvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected WebApplicationContext webApplicationContext;

	@Autowired
	protected LoanApplyService applyLoanService;
	
	@Autowired
	protected CountryResolverService countryResolverService;
	
	@Before
	public void setUp() {
		Mockito.reset(applyLoanService);
		Mockito.reset(countryResolverService);

		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	final ResultActions postObject(ApplyLoanDTO dto) throws Exception, JsonProcessingException {
		return mvc.perform(MockMvcRequestBuilders.post("/api/v1/loan")
				.contentType(TestControllerUtil.APPLICATION_JSON_UTF8)
		        .locale(Locale.ENGLISH)
		        .content(objectMapper.writeValueAsBytes(dto)));
	}
}
