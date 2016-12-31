/**
 * 
 */
package org.dsu.dao;

import java.util.List;

import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** The DAO works with the {@link Loan} entity.
 *
 * @author nescafe
 */
public interface LoanDAO extends JpaRepository<Loan, Long> {

	/** Finds loans entities by LoanStatus.
	 * 
	 * @param status    The LoanStatus value.
	 * @param page      Paging information.
	 * @return          The list of loans, sorted by {@link Loan#created} field in ascending order.
	 */
	List<Loan> findByStatus(LoanStatus status, Pageable page);
}
