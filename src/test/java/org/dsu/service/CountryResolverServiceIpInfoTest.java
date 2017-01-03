/**
 * 
 */
package org.dsu.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.dsu.component.RemoteAddressResolver;
import org.dsu.service.countryresolver.CountryResolverService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Tests getting country code by ip.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceConfig.class })
@ActiveProfiles("test")
public class CountryResolverServiceIpInfoTest {

	private static final ParameterizedTypeReference<Map<String, Object>> type = new ParameterizedTypeReference<Map<String, Object>>() {
	};

	@Autowired
	private RemoteAddressResolver remoteAddressResolver;

	@Autowired
	private CountryResolverService countryResolverService;

	@Autowired
	private RestTemplate restTemplate;

	@Before
	public void setUp() {
		Mockito.reset(remoteAddressResolver);
		Mockito.reset(restTemplate);
	}

	@Test
	public void givenIP_WhenResolveCode_ThenReturnCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ip", "8.8.8.8");
		map.put("country", "US");

		when(remoteAddressResolver.getRemoteIP()).thenReturn("8.8.8.8");
		when(restTemplate.exchange(eq("http://ipinfo.io/8.8.8.8"), any(HttpMethod.class), any(HttpEntity.class),
		        eq(type))).thenReturn(new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK));

		String countryCode = countryResolverService.resolveCode();
		assertEquals("US", countryCode);

		verify(remoteAddressResolver, times(1)).getRemoteIP();
		verifyNoMoreInteractions(remoteAddressResolver);
		verify(restTemplate, times(1)).exchange(eq("http://ipinfo.io/8.8.8.8"), any(HttpMethod.class),
		        any(HttpEntity.class), eq(type));
		verifyNoMoreInteractions(restTemplate);
	}

	@Test(expected = IllegalStateException.class)
	public void givenNullIP_WhenResolveCode_ThenThrowException() {
		when(remoteAddressResolver.getRemoteIP()).thenReturn(null);
		countryResolverService.resolveCode();
	}

	@Test
	public void givenNoReturnCountryCode_WhenResolveCode_ThenReturnDefaultCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ip", "8.8.8.8");

		when(remoteAddressResolver.getRemoteIP()).thenReturn("8.8.8.8");
		when(restTemplate.exchange(eq("http://ipinfo.io/8.8.8.8"), any(HttpMethod.class), any(HttpEntity.class),
		        eq(type))).thenReturn(new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK));

		String countryCode = countryResolverService.resolveCode();
		assertEquals("LV", countryCode);

		verify(remoteAddressResolver, times(1)).getRemoteIP();
		verifyNoMoreInteractions(remoteAddressResolver);
		verify(restTemplate, times(1)).exchange(eq("http://ipinfo.io/8.8.8.8"), any(HttpMethod.class),
		        any(HttpEntity.class), eq(type));
		verifyNoMoreInteractions(restTemplate);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void givenRestTemplateFail_WhenResolveCode_ThenReturnDefaultCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ip", "8.8.8.8");

		when(remoteAddressResolver.getRemoteIP()).thenReturn("8.8.8.8");
		when(restTemplate.exchange(eq("http://ipinfo.io/8.8.8.8"), any(HttpMethod.class), any(HttpEntity.class),
		        eq(type))).thenThrow(Exception.class);

		String countryCode = countryResolverService.resolveCode();
		assertEquals("LV", countryCode);

		verify(remoteAddressResolver, times(1)).getRemoteIP();
		verifyNoMoreInteractions(remoteAddressResolver);
		verify(restTemplate, times(1)).exchange(eq("http://ipinfo.io/8.8.8.8"), any(HttpMethod.class),
		        any(HttpEntity.class), eq(type));
		verifyNoMoreInteractions(restTemplate);
	}
}
