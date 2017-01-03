/**
 * 
 */
package org.dsu.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * The class describes input data for the loan.
 *
 * @author nescafe
 */
public class ApplyLoanDTO {

	@NotNull(message = "musnt.be.empty")
	@Max(value = 1000000, message = "max.amount")
	@Min(value = 1, message = "min.amount")
	private BigDecimal amount;

	@NotBlank(message = "musnt.be.empty")
	@Size(max = 10000, message = "size.max.10000")
	private String term;

	@NotBlank(message = "musnt.be.empty")
	@Size(max = 1000, message = "size.max.1000")
	private String firstName;

	@NotBlank(message = "musnt.be.empty")
	@Size(max = 1000, message = "size.max.1000")
	private String surName;

	public ApplyLoanDTO() {

	}

	public ApplyLoanDTO(BigDecimal amount, String term, String firstName, String surName) {
		super();
		this.amount = amount;
		this.term = term;
		this.firstName = firstName;
		this.surName = surName;
	}

	/**
	 * Gets amount value of the loan.
	 * 
	 * @return
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Gets the term of the loan.
	 * 
	 * @return
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Gets the first name of the person who applies the loan.
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets the surname of the person who applies the loan.
	 * 
	 * @return
	 */
	public String getSurName() {
		return surName;
	}

	@Override
	public String toString() {
		return "ApplyLoanDTO [amount=" + amount + ", term=" + term + ", firstName=" + firstName + ", surName=" + surName
		        + "]";
	}
}
