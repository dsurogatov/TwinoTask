/**
 * 
 */
package org.dsu.service.countryresolver;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dsu.component.RemoteAddressResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Implemets to get country code from the <a>ipinfo.io</a> site by the IP address.
 *
 * @author nescafe
 */
@Service
public class CountryResolverServiceIPInfoImpl implements CountryResolverService {

	private static final Logger LOG = LoggerFactory.getLogger(CountryResolverServiceIPInfoImpl.class);
	private static final String COUNTRY_KEY = "country";
	private static final String DEFAULT_COUNTRY_CODE = "LV";

	private final HttpEntity<String> entity;
	private final ParameterizedTypeReference<Map<String, Object>> type = new ParameterizedTypeReference<Map<String, Object>>() {
	};

	@Value("${ipinfo.url:ipinfo.io}")
	private String ipInfoUrl;

	@Resource(name="${RemoteAddressResolver.name:remoteAddressResolver}")
	private RemoteAddressResolver remoteAddressResolver;

	@Autowired
	private RestTemplate ipInfoRestTemplate;

	public CountryResolverServiceIPInfoImpl() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		entity = new HttpEntity<>(headers);
	}

	@Override
	public String resolveCode() {
		String ip = remoteAddressResolver.getRemoteIP();
		if (StringUtils.isEmpty(ip)) {
			throw new IllegalStateException("The remote IP must not be empty.");
		}

		try {
			ResponseEntity<Map<String, Object>> result = ipInfoRestTemplate.exchange(buildUrl(ip), HttpMethod.GET,
			        entity, type);
			Map<String, Object> returnedMap = result.getBody();
			if(LOG.isDebugEnabled()) {
				LOG.debug("" + returnedMap);
			}
			if (returnedMap.containsKey(COUNTRY_KEY)) {
				return returnedMap.get(COUNTRY_KEY).toString();
			} else {
				return DEFAULT_COUNTRY_CODE;
			}
		} catch (Exception e) {
			LOG.warn("Exception while getting IP info: '{}'", e.getMessage());
			return DEFAULT_COUNTRY_CODE;
		}
	}

	private String buildUrl(String ip) {
		return "http://" + ipInfoUrl + "/" + ip;
	}

}
