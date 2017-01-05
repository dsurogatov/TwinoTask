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

	/** Finds the {@link BlackList} by {@link Person#getFirstName()} and {@link Person#getSurName()}.
	 * 
	 * @param firstName  The firstname of the person.
	 * @param surName    The surname of the person.
	 * @return           The {@link BlackList}.
	 */
	BlackList findByPersonFirstNameAndPersonSurName(String firstName, String surName);
}
