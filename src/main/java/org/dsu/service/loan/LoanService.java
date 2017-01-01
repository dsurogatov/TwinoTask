/**
 * 
 */
package org.dsu.service.loan;

import java.util.List;

import org.dsu.dto.LoanDTO;
import org.springframework.data.domain.Pageable;

/** Defines the service interface for working with loans.
 * 
 * @author nescafe
 */
public interface LoanService {

	/** Loads approved loans using pagination.
	 * 
	 * @param     The pagination information.
	 * @return    The list of approved loans.
	 * 
	 * @throws {@link IllegalArgumentException}  
	 *            if the page is null or {@link Pageable#getPageSize()} more than {@link Constant#PAGE_MAX_SIZE}
	 */
	List<LoanDTO> findApprovedLoans(Pageable page);
	
	/** Loads approved loans by the person's id using pagination.
	 * 
	 * @param personId  The id of the person.
	 * @param page      The pagination information.
	 * @return
	 * 
	 * @throws {@link IllegalArgumentException}  
	 *            if the page is null or {@link Pageable#getPageSize()} more than {@link Constant#PAGE_MAX_SIZE}.
	 *            if personId is null.
	 */
	List<LoanDTO> findApprovedLoansByPersonId(Long personId, Pageable page);
}
