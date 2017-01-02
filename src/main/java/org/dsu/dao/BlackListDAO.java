/**
 * 
 */
package org.dsu.dao;

import org.dsu.domain.BlackList;
import org.dsu.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/** The DAO works with the {@link BlackList} entity.
 *
 * @author nescafe
 */
public interface BlackListDAO extends JpaRepository<BlackList, Long> {

	/** Finds the {@link BlackList} by {@link Person}.
	 * 
	 * @param person  The {@link Person}.
	 * @return        The {@link BlackList}.
	 */
	BlackList findByPerson(Person person);
}
