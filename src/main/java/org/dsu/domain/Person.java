/**
 * 
 */
package org.dsu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/** The entity for persons.
 *
 * @author nescafe
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"firstname", "surname"}))
public class Person extends AbstractIdableEntity {

	@Column(nullable = false, length = 1000)
	private String firstName; 
	
	@Column(nullable = false, length = 1000)
	private String surName;

	/** Gets the first name of the person.
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/** Sets the first name of the person.
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/** Gets the surname of the person.
	 * 
	 * @return
	 */
	public String getSurName() {
		return surName;
	}

	/** Sets the surname of the person.
	 * 
	 * @param surName
	 */
	public void setSurName(String surName) {
		this.surName = surName;
	} 
}
