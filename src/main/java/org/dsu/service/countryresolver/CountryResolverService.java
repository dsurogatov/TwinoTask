/**
 * 
 */
package org.dsu.service.countryresolver;

/** Defines an object to provide information about country.
 *
 * @author nescafe
 */
public interface CountryResolverService {

	/** Resolves country code in the format ISO-2 (e.g 'US').
	 * 
	 * @return  The country code.
	 */
	String resolveCode();
}
