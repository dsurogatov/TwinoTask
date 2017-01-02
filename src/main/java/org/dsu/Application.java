package org.dsu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class Application {
	
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasename("i18n/messages");
	    messageSource.setDefaultEncoding("UTF-8");
	    messageSource.setUseCodeAsDefaultMessage(true);
	    return messageSource;
	} 
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
