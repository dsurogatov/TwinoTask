package org.dsu.service;

import org.dsu.component.RemoteAddressResolver;
import org.dsu.dao.BlackListDAO;
import org.dsu.dao.CountryDAO;
import org.dsu.dao.LoanDAO;
import org.dsu.dao.PersonDAO;
import org.dsu.service.blacklist.PersonBlackListService;
import org.dsu.service.blacklist.PersonBlackListServiceDatabaseImpl;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"org.dsu.service"})
@Profile("test")
public class ServiceConfig {
	
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasename("i18n/messages");
	    messageSource.setDefaultEncoding("UTF-8");
	    messageSource.setUseCodeAsDefaultMessage(true);
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

	@Bean
	public BlackListDAO blackListDao() {
		return Mockito.mock(BlackListDAO.class);
	}

	@Bean
	public CountryDAO countryDao() {
		return Mockito.mock(CountryDAO.class);
	}
	
	@Bean
	public RemoteAddressResolver remoteAddressResolver() {
		return Mockito.mock(RemoteAddressResolver.class);
	}

	@Bean
	public PersonBlackListService personBlackListService() {
		return Mockito.mock(PersonBlackListService.class);
	}
	
	@Bean
	public PersonBlackListService personBlackListServiceDatabaseImpl() {
		return new PersonBlackListServiceDatabaseImpl();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return Mockito.mock(RestTemplate.class);
	}
}
