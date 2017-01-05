package org.dsu.controller;

import org.dsu.service.countryresolver.CountryResolverService;
import org.dsu.service.loan.LoanApplicationService;
import org.dsu.service.loan.LoanService;
import org.dsu.service.validation.LoanApplicationLimitRequestService;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
		"org.dsu.controller",
})
@EnableSpringDataWebSupport
@Profile("test")
public class WebAppConfig extends WebMvcConfigurerAdapter {
	
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	
	@Bean
    public LoanService loanService() {
        return Mockito.mock(LoanService.class);
    }

	@Bean
	public LoanApplicationService loanApplicationService() {
		return Mockito.mock(LoanApplicationService.class);
	}

	@Bean
	public CountryResolverService countryResolverService() {
		return Mockito.mock(CountryResolverService.class);
	}
	
	@Bean
	public LoanApplicationLimitRequestService loanApplicationLimitRequestService() {
		return Mockito.mock(LoanApplicationLimitRequestService.class);
	}
	
	@Bean
    public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper;
    }
	
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
	} 
	
}
