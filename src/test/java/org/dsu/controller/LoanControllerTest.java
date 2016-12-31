package org.dsu.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.dsu.dto.LoanDTO;
import org.dsu.dto.PersonDTO;
import org.dsu.service.loan.LoanService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebAppConfig.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class LoanControllerTest {

	MockMvc mvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	private LoanService loanService;
	
	@Before
	public void setUp() {
		Mockito.reset(loanService);
		
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void whenAskApprovedLoans_ThenReturnLoans() throws Exception {
		List<LoanDTO> list = new ArrayList<>();
		LocalDateTime createdDateTime = LocalDateTime.of(2016, 1, 1, 14, 16, 45);
		list.add(new LoanDTO(1L, BigDecimal.valueOf(12.45), "Term", new PersonDTO(1L, "first", "second"), createdDateTime));
		list.add(new LoanDTO(2L, BigDecimal.valueOf(212.45), "Term2", new PersonDTO(2L, "first2", "second2"), createdDateTime));
		
		when(loanService.findAllApproved(any(Pageable.class))).thenReturn(list);

		mvc.perform(MockMvcRequestBuilders.get("/api/v1/loan/approved").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(ControllerTestUtil.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].amount", is(BigDecimal.valueOf(12.45).doubleValue())))
            .andExpect(jsonPath("$[0].term", is("Term")))
            .andExpect(jsonPath("$[0].person.id", is(1)))
            .andExpect(jsonPath("$[0].person.firstName", is("first")))
            .andExpect(jsonPath("$[0].person.surName", is("second")))
            .andExpect(jsonPath("$[0].created", is("2016-01-01 14:16:45")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].amount", is(BigDecimal.valueOf(212.45).doubleValue())))
            .andExpect(jsonPath("$[1].term", is("Term2")))
            .andExpect(jsonPath("$[1].person.id", is(2)))
            .andExpect(jsonPath("$[1].person.firstName", is("first2")))
            .andExpect(jsonPath("$[1].person.surName", is("second2")))
            .andExpect(jsonPath("$[1].created", is("2016-01-01 14:16:45")))
            ;

		verify(loanService, times(1)).findAllApproved(any(Pageable.class));
        verifyNoMoreInteractions(loanService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void givenLoanServiceFails_whenAskApprovedLoans_ThenReturtInternalServerError() throws Exception {

		// mock service to thrown exception
		when(loanService.findAllApproved(any(Pageable.class))).thenThrow(Exception.class);
		
		// perform a request
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/loan/approved").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isInternalServerError())
		.andExpect(content().contentType(ControllerTestUtil.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.message", is("Internal server error.")))
		;
		
		verify(loanService, times(1)).findAllApproved(any(Pageable.class));
        verifyNoMoreInteractions(loanService);
	}
}
