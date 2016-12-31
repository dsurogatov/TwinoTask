/**
 * 
 */
package org.dsu.dto;

/** The DTO's object, which represents {@link org.dsu.domain.Person}}
 * 
 * @author nescafe
 */
public class PersonDTO extends AbstractIdableDTO {

	private final String firstName;
	private final String surName;
	
	public PersonDTO(Long id, String firstName, String surName) {
		this.id = id;
		this.firstName = firstName;
		this.surName = surName;
	}

	/** Gets the first name of the person.
	 * 
	 * @return    The String value.
	 */
	public String getFirstName() {
		return firstName;
	}

	/** Gets the surname of the person.
	 * 
	 * @return    The String value
	 */
	public String getSurName() {
		return surName;
	}
}
