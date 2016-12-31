/**
 * 
 */
package org.dsu.domain;

/** Marks an entity class, which contains Long id field. 
 * 
 * @author nescafe
 */
public interface IdableEntity {

	/** The value of the identificator of the entity class. 
	 * 
	 * @return    The Long value.
	 */
	Long getId();
}
