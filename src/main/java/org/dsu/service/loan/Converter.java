/**
 * 
 */
package org.dsu.service.loan;

import org.dsu.domain.Loan;
import org.dsu.domain.Person;
import org.dsu.dto.LoanDTO;
import org.dsu.dto.PersonDTO;

/** Converts entities to DTOs and vice versa.
 *
 * @author nescafe
 */
final class Converter {
	
	private Converter() {
		
	}
	
	/** Converts the {@link Loan} to {@link LoanDTO}.
	 * 
	 * @param entity  The {@link Loan} entiy.
	 * @return        The {@link LoanDTO} object.
	 */
	static LoanDTO toLoanDTO(Loan entity) {
		Person loanPerson = entity.getPerson();
		PersonDTO loanPersonDTO = new PersonDTO(loanPerson.getId(), loanPerson.getFirstName(), loanPerson.getSurName());
		String countryCode = (entity.getCountry() == null ? null : entity.getCountry().getCode());
		return new LoanDTO(entity.getId(), entity.getAmount(), entity.getTerm(), loanPersonDTO, 
				entity.getCreated(), entity.getStatus().name().toLowerCase(), countryCode);
	}

}
