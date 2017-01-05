/**
 * 
 */
package org.dsu.dao;

import static org.junit.Assert.assertEquals;

import org.dsu.config.DataSourceConfig;
import org.dsu.config.JPAConfig;
import org.dsu.domain.BlackList;
import org.dsu.domain.Person;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Tests the {@link BlackListDAO}.
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
public class BlackListDAOTest {
	
	@Autowired
	private PersonDAO personDao;
	
	@Autowired
	private BlackListDAO blackListDao;

	@After
	public void cleanData() {
		blackListDao.deleteAllInBatch();
		personDao.deleteAllInBatch();
	}
	
	@Test
	public void givenBlackList_WhenFindByPerson_ThenReturnBlackList () {
		Person person = new Person();
		person.setFirstName("firstName");
		person.setSurName("surName");
		person = personDao.save(person);
		
		BlackList blackList = new BlackList();
		blackList.setPerson(person);
		blackList = blackListDao.save(blackList);
		
		BlackList foundBlackList = blackListDao.findByPersonFirstNameAndPersonSurName("firstName", "surName");
		assertEquals(blackList.getId(), foundBlackList.getId());
		assertEquals(person.getId(), foundBlackList.getPerson().getId());
	}
}
