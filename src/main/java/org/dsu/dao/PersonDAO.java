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

}
