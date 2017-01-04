/**
 * 
 */
package org.dsu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/** The entity contains information about the country.
 *
 * @author nescafe
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
public class Country extends AbstractIdableEntity {

	@Column(nullable = false, length = 2)
	private String code;

	/** Gets the country code.
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/** Sets the country code.
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}
}
