package org.dsu.service;

import org.dsu.dao.LoanDAO;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackages = {
		"org.dsu.service",
})
@Profile("test")
public class ServiceConfig {

	@Bean
    public LoanDAO loanDao() {
        return Mockito.mock(LoanDAO.class);
    }
}
