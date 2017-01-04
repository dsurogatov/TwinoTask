/**
 * 
 */
package org.dsu.service.loan;

import org.dsu.dto.LoanDTO;

/** The service creates and validates new loans.
 *
 * @author nescafe
 */
public interface LoanApplyService {

	/** Creates a new Loan.
	 *  
	 * @param dto          The dto is come from the client.
	 * @return             The created loan.
	 * 
	 * @throws     {@link ApplicationException} if the person, who applies the loan, is in the blacklist.
	 */
	LoanDTO apply(LoanDTO dto);
}
