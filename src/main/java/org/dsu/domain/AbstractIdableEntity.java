/**
 * 
 */
package org.dsu.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/** Defines the 'id' field for ancestors.
 * 
 * @author nescafe
 */
@MappedSuperclass
public abstract class AbstractIdableEntity implements IdableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/* (non-Javadoc)
	 * @see org.dsu.domain.IdableEntity#getId()
	 */
	public Long getId() {
		return id;
	}

	/** Sets the value of the 'id' field.
	 * 
	 * @param id   
	 */
	public void setId(Long id) {
		this.id = id;
	}
}
