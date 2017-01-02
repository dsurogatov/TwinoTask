/**
 * 
 */
package org.dsu.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.dsu.config.DataSourceConfig;
import org.dsu.config.JPAConfig;
import org.dsu.domain.Person;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Tests the PersonDAO.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class, JPAConfig.class, DataSourceConfig.class })
@ActiveProfiles("test")
@TestPropertySource(locations = {
		"classpath:test-jdbc.properties",
		"classpath:test-hibernate.properties"
		})
public class PersonDAOTest {

	@Autowired
	private PersonDAO personDao;

	@After
	public void cleanData() {
		personDao.deleteAllInBatch();
	}
	
	@Test
	public void givenPerson_WhenFindByFirstNameAndSurName_ThenReturnPerson () {
		// create the person
		Person person = new Person();
		person.setFirstName("firstName");
		person.setSurName("surName");
		personDao.save(person);
		
		// find the person by the first and sur names.
		Person foundPerson = personDao.findByFirstNameAndSurName("firstName", "surName");
		// check return person
		assertNotNull(foundPerson.getId());
		assertEquals("firstName", foundPerson.getFirstName());
		assertEquals("surName", foundPerson.getSurName());
	}
}
