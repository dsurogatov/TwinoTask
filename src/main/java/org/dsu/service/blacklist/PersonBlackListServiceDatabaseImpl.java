/**
 * 
 */
package org.dsu.service.blacklist;

import javax.transaction.Transactional;

import org.dsu.dao.BlackListDAO;
import org.dsu.domain.BlackList;
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

	@Override
	public boolean inList(String firstName, String surName) {
		BlackList blackList = blackListDao.findByPersonFirstNameAndPersonSurName(firstName, surName);
		if (blackList == null) {
			return false;
		} else {
			return true;
		}
	}

}
