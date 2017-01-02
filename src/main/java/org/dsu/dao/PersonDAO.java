/**
 * 
 */
package org.dsu.dao;

import org.dsu.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/** The DAO works with the {@link Person} entity.
 *
 * @author nescafe
 */
public interface PersonDAO extends JpaRepository<Person, Long> {

	/** Finds the person by his firstName and surName.
	 * 
	 * @param firstName   The firstName value.
	 * @param surName     The surName value.
	 * @return            The person's entity. 
	 */
	Person findByFirstNameAndSurName(String firstName, String surName);
}
