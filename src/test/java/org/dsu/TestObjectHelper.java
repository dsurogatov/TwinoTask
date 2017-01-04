/**
 * 
 */
package org.dsu;

import java.math.BigDecimal;

import org.dsu.domain.Country;
import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.dsu.domain.Person;
import org.dsu.dto.LoanDTO;
import org.dsu.dto.PersonDTO;

/**
 * Helps to create objects.
 *
 * @author nescafe
 */
public final class TestObjectHelper {

	public static final Page PAGE_DEFAULT = new Page(0, 20, null);
	public static final Person PERSON_DEFAULT = person(null, "firstName", "surName");

	public static Loan approvedLoan(Long id, double amount, String term, Person person, Country country) {
		Loan loan = new Loan();
		loan.setId(id);
		loan.setAmount(BigDecimal.valueOf(amount));
		loan.setTerm(term);
		loan.setStatus(LoanStatus.APPROVED);
		loan.setPerson(person);
		loan.setCountry(country);
		return loan;
	}

	public static Person person(Long id, String firstName, String surName) {
		Person person = new Person();
		person.setId(id);
		person.setFirstName(firstName);
		person.setSurName(surName);
		return person;
	}
	
	public static Country country(Long id, String code) {
		Country country = new Country();
		country.setId(id);
		country.setCode(code);
		return country;
	}

	public static LoanDTO loanDto(BigDecimal amount, String term, String firstName, String surName) {
		return loanDto(amount, term, firstName, surName, null);
	}

	public static LoanDTO loanDto(BigDecimal amount, String term, String firstName, String surName, String countryCode) {
		LoanDTO loanDto = new LoanDTO(null, amount, term, new PersonDTO(null, firstName, surName), null, null, countryCode);
		return loanDto;
	}
}
