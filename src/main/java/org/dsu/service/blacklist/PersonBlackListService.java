/**
 * 
 */
package org.dsu.service.blacklist;

/** The interface defines method to check the {@link Person} in the blacklist for applying loans.
 *
 * @author nescafe
 */
public interface PersonBlackListService {

	/** Checks the person with the id is in the blacklist.
	 * 
	 * @param personId  The person's id.
	 * @return          true if it is the blacklist
	 */
	boolean inList(long personId);
}
