/**
 * 
 */
package org.dsu.service.blacklist;

/** The interface defines method to check the {@link Person} in the blacklist for applying loans.
 *
 * @author nescafe
 */
public interface PersonBlackListService {

	/** Checks the person is in the blacklist
	 * by the firstName and the surName.
	 * 
	 * @param firstName  The firstname.
	 * @param surName  The surname.
	 * @return          true if it is the blacklist
	 */
	boolean inList(String firstName, String surName);
}
