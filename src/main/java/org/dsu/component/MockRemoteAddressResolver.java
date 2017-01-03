/**
 * 
 */
package org.dsu.component;

import java.util.Random;

import org.springframework.stereotype.Service;

/** Implements resolving remote IP using predefinded addresses.
 *
 * @author nescafe
 */
@Service(value = "mockRemoteAddressResolver")
public class MockRemoteAddressResolver implements RemoteAddressResolver {
	
	private final String[] ADDRESSES = {"8.8.8.8", "213.180.193.3", "178.172.160.4"};
	private final Random random = new Random(); 

	@Override
	public String getRemoteIP() {
		return ADDRESSES[random.nextInt(3)];
	}

}
