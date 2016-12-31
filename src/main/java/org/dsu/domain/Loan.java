/**
 * 
 */
package org.dsu.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The loan entity.
 * 
 * @author nescafe
 */
@Entity
public class Loan extends AbstractIdableEntity {

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false, length = 10000)
	private String term;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private LoanStatus status = LoanStatus.NEW;

	@ManyToOne
	@JoinColumn(name = "person_id", nullable = false)
	private Person person;

	@Column(nullable = false)
	private LocalDateTime created = LocalDateTime.now();

	/**
	 * Gets the amount value of the loan.
	 * 
	 * @return
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Sets the amount value of the loan.
	 * 
	 * @param amount
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	 * Sets the term of the loan.
	 * 
	 * @param term
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * Gets the status of the loan.
	 * 
	 * @return
	 */
	public LoanStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status of the loan.
	 * 
	 * @param status
	 */
	public void setStatus(LoanStatus status) {
		this.status = status;
	}

	/**
	 * Gets the person of the loan.
	 * 
	 * @return
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * Sets the person of the loan.
	 * 
	 * @param person
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/** Gets date and time of creating the entity.
	 * 
	 * @return
	 */
	public LocalDateTime getCreated() {
		return created;
	}

}
