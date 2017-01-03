/**
 * 
 */
package org.dsu.component;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Resovles the remote IP from {@link HttpServletRequest}.
 *
 * @author nescafe
 */
@Component(value = "requestRemoteAddressResolver")
public class RequestRemoteAddressResolver implements RemoteAddressResolver {

	@Override
	public String getRemoteIP() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
		        .getRequest();
		return request.getRemoteAddr();
	}

}
