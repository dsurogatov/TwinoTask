/**
 * 
 */
package org.dsu.dao;

import org.dsu.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

/** The DAO works with the {@link Country} entity.
 *
 * @author nescafe
 */
public interface CountryDAO extends JpaRepository<Country, Long> {

	/** Finds the {@link Country} by the code.
	 * 
	 * @param code  The two letters code.
	 * @return      The found entity.
	 */
	Country findByCode(String code);
}
