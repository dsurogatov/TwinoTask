/**
 * 
 */
package org.dsu.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/** The black list entity.
 *
 * @author nescafe
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"person_id"}))
public class BlackList extends AbstractIdableEntity {

	@ManyToOne
	@JoinColumn(name = "person_id", nullable = false)
	private Person person;

	/** Gets the {@link Person} entity.
	 * 
	 * @return
	 */
	public Person getPerson() {
		return person;
	}

	/** Sets the {@link Person} entity.
	 * 
	 * @param person
	 */
	public void setPerson(Person person) {
		this.person = person;
	}
	
}
