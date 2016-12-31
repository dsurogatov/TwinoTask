/**
 * 
 */
package org.dsu.dto;

/** The abstract class contains the 'id' field.
 * 
 * @author nescafe
 */
public class AbstractIdableDTO implements IdableDTO {
	
	protected Long id;

	/* (non-Javadoc)
	 * @see org.dsu.dto.IdableDTO#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

}
