/**
 * 
 */
package org.dsu;

import java.math.BigDecimal;

import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.dsu.domain.Person;

/** Helps to create objects.
 *
 * @author nescafe
 */
public final class TestObjectHelper {

	public static final Page PAGE_DEFAULT = new Page(0, 20, null);
	public static final Person PERSON_DEFAULT = person(null, "firstName", "surName");
	
//	static {
//		PERSON_DEFAULT.setFirstName("firstName");
//		PERSON_DEFAULT.setSurName("surName");
//	}
	
	public static Loan approvedLoan(Long id, double amount, String term, Person person) {
		Loan loan = new Loan();
		loan.setId(id);
		loan.setAmount(BigDecimal.valueOf(amount));
		loan.setTerm(term);
		loan.setStatus(LoanStatus.APPROVED);
		loan.setPerson(person);
		return loan;
	}
	
	public static Person person(Long id, String firstName, String surName) {
		Person person = new Person();
		person.setId(id);
		person.setFirstName(firstName);
		person.setSurName(surName);
		return person;
	}
}
