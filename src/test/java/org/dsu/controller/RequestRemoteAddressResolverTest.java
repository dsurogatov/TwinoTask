/**
 * 
 */
package org.dsu.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.dsu.component.RequestRemoteAddressResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** Tests the {@link RequestRemoteAddressResolver}.
 *
 * @author nescafe
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestRemoteAddressResolverTest {
	
	@Mock
	private HttpServletRequest request;

	@Test
	public void whenGetRemoteIP_ThenReturnIP() {
		// mock the getRemoteAddr
		when(request.getRemoteAddr()).thenReturn("1.1.1.1");
		
		// create ServletRequestAttributes
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(request); 
		// set requestAttributes to RequestContextHolder
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
		
		// run the resolver
		RequestRemoteAddressResolver resolver = new RequestRemoteAddressResolver();
		String ip = resolver.getRemoteIP();
		
		// check answer
		assertEquals("1.1.1.1", ip);
		
	}
}
