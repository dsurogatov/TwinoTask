/**
 * 
 */
package org.dsu.component;

/** Defines an object to provide information about te client ip.
 *
 * @author nescafe
 */
public interface RemoteAddressResolver {

	/** Resolves the remote ip of the client.
	 * 
	 * @return The string representation of ip address.
	 */
	String getRemoteIP();
}
