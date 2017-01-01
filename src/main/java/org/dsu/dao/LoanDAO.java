/**
 * 
 */
package org.dsu.dao;

import java.util.List;

import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.dsu.domain.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** The DAO works with the {@link Loan} entity.
 *
 * @author nescafe
 */
public interface LoanDAO extends JpaRepository<Loan, Long> {

	/** Finds loans entities by the LoanStatus.
	 * 
	 * @param status    The LoanStatus value.
	 * @param page      The pagination information.
	 * @return          The list of loans.
	 */
	List<Loan> findByStatus(LoanStatus status, Pageable page);
	
	/** Finds loans by the LoanStatus and the Person
	 * 
	 * @param status    The LoanStatus value.
	 * @param person    The Person.
	 * @param page      The pagination information.
	 * @return          The list of loans.
	 */
	List<Loan> findByStatusAndPerson(LoanStatus status, Person person, Pageable page);
}
