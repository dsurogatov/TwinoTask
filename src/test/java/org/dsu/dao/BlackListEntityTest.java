/**
 * 
 */
package org.dsu.dao;

import javax.transaction.Transactional;

import org.dsu.config.DataSourceConfig;
import org.dsu.config.JPAConfig;
import org.dsu.domain.BlackList;
import org.dsu.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Checks database constraints of the {@link BlackList} entity.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class, JPAConfig.class, DataSourceConfig.class })
@Transactional
@Rollback
@ActiveProfiles("test")
public class BlackListEntityTest {

	@Autowired
	private BlackListDAO blackListDao;
	
	@Autowired
	private PersonDAO personDao;
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenNullPerson_WhenSaveBlackList_ThenThrowException() {
		blackListDao.saveAndFlush(new BlackList());
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenDuplicatePerson_WhenSaveBlackList_ThenThrowException() {
		Person person = new Person();
		person.setFirstName("firstName");
		person.setSurName("surName");
		person = personDao.saveAndFlush(person);
		
		BlackList blackList1 = new BlackList();
		blackList1.setPerson(person);
		blackListDao.saveAndFlush(blackList1);
		
		BlackList blackList2 = new BlackList();
		blackList2.setPerson(person);
		blackListDao.saveAndFlush(blackList2);
	}
}
