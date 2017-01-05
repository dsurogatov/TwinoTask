/**
 * 
 */
package org.dsu.service.validation;

/** Defines an object, which validates 
 * that the limit of requests loan applications is reached. 
 *
 * @author nescafe
 */
public interface LoanApplicationLimitRequestService {
	
	/** Checks if the limit requests is reached the from particular country.
	 * 
	 * @param countryCode  The country code in 2 letters format.
	 * @return             true - if the limit is reached.
	 */
	boolean limitReachedByCountry(String countryCode);

}
