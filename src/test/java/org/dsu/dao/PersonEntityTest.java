/**
 * 
 */
package org.dsu.dao;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.dsu.config.DataSourceConfig;
import org.dsu.config.JPAConfig;
import org.dsu.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Checks database constraints of the {@link Person} entity.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class, JPAConfig.class, DataSourceConfig.class })
@Transactional
@Rollback
@ActiveProfiles("test")
public class PersonEntityTest {
	
	@Autowired
	private PersonDAO personDao;
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenNullFirstName_WhenSavePerson_ThenThrowException() {
		Person person = new Person();
		person.setFirstName(null);
		person.setSurName("surName");
		personDao.saveAndFlush(person);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void givenNullSurName_WhenSavePerson_ThenThrowException() {
		Person person = new Person();
		person.setFirstName("firstName");
		person.setSurName(null);
		personDao.saveAndFlush(person);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenLongLengthFirstName_WhenSavePerson_ThenThrowException() {
		Person person = new Person();
		person.setFirstName(RandomStringUtils.random(1001));
		person.setSurName("surName");
		personDao.saveAndFlush(person);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenLongLengthSurName_WhenSavePerson_ThenThrowException() {
		Person person = new Person();
		person.setFirstName("firstName");
		person.setSurName(RandomStringUtils.random(1001));
		personDao.saveAndFlush(person);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenTwoPersonsWithSameNames_WhenSavePerson_ThenThrowException() {
		Person person = new Person();
		person.setFirstName("firstName");
		person.setSurName("surName");
		personDao.saveAndFlush(person);
		
		person = new Person();
		person.setFirstName("firstName");
		person.setSurName("surName");
		personDao.saveAndFlush(person);
	}
}
