/**
 * 
 */
package org.dsu.service.blacklist;

import javax.transaction.Transactional;

import org.dsu.dao.BlackListDAO;
import org.dsu.dao.PersonDAO;
import org.dsu.domain.BlackList;
import org.dsu.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implements the interface {@link PersonBlackListService}. Having the person blacklist in the database.
 *
 * @author nescafe
 */
@Service
@Transactional
public class PersonBlackListServiceDatabaseImpl implements PersonBlackListService {

	@Autowired
	private BlackListDAO blackListDao;

	@Autowired
	private PersonDAO personDao;

	@Override
	public boolean inList(long personId) {
		Person person = personDao.findOne(personId);
		if (person == null) {
			return false;
		}

		BlackList blackList = blackListDao.findByPerson(person);
		if (blackList == null) {
			return false;
		} else {
			return true;
		}
	}

}
