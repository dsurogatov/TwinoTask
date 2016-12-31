/**
 * 
 */
package org.dsu.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/** The DTO's object, which represents {@link org.dsu.domain.Loan}} 
 * 
 * @author nescafe
 */
public class LoanDTO extends AbstractIdableDTO {

	private final BigDecimal amount;
	private final String term; 
	
	@JsonProperty("person")
	private final PersonDTO personDto;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") 
	private final LocalDateTime created;
	
	public LoanDTO(Long id, BigDecimal amount, String term, PersonDTO dto, LocalDateTime created) {
		this.id = id;
		this.amount = amount;
		this.term = term; 
		this.personDto = dto;
		this.created = created;
	}

	/** Gets the amount value of the loan.
	 * 
	 * @return    The decimal value.
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/** Gets the term of the loan.
	 * 
	 * @return    The String value.
	 */
	public String getTerm() {
		return term;
	}

	/** Gets the person of the loan.
	 * 
	 * @return   The {@link PersonDTO} instance. 
	 */
	public PersonDTO getPersonDto() {
		return personDto;
	}

	/** Gets creating date and time of the loan.
	 * 
	 * @return
	 */
	public LocalDateTime getCreated() {
		return created;
	}
}
