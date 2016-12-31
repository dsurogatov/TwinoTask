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

	/** Loads all approved loans using pagination.
	 * 
	 * @param     Pagination information.
	 * @return    The list of approved loans.
	 */
	List<LoanDTO> findAllApproved(Pageable page);
}
