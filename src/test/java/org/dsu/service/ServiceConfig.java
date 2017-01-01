package org.dsu.service;

import org.dsu.dao.LoanDAO;
import org.dsu.dao.PersonDAO;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@ComponentScan(basePackages = {"org.dsu.service"})
@Profile("test")
public class ServiceConfig {
	
	@Bean
	public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	    messageSource.setBasename("/i18/message");
	    messageSource.setDefaultEncoding("UTF-8");
	    return messageSource;
	} 

	@Bean
    public LoanDAO loanDao() {
        return Mockito.mock(LoanDAO.class);
    }
	
	@Bean
	public PersonDAO personDao() {
		return Mockito.mock(PersonDAO.class);
	}
}
