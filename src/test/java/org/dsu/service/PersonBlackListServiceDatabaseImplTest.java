/**
 * 
 */
package org.dsu.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.dsu.dao.BlackListDAO;
import org.dsu.domain.BlackList;
import org.dsu.service.blacklist.PersonBlackListService;
import org.dsu.service.blacklist.PersonBlackListServiceDatabaseImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Tests the {@link PersonBlackListServiceDatabaseImpl}.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceConfig.class })
@ActiveProfiles("test")
public class PersonBlackListServiceDatabaseImplTest {

	@Autowired
	private PersonBlackListService personBlackListServiceDatabaseImpl;
	
	@Autowired
	private BlackListDAO blackListDao;

	@Before
	public void setUp() {
		Mockito.reset(blackListDao);
	}
	
	@Test
	public void givenPersonNotInBlackList_WhenRunInList_ThenReturnFalse() {
		when(blackListDao.findByPersonFirstNameAndPersonSurName(anyString(), anyString())).thenReturn(null);
		
		boolean result = personBlackListServiceDatabaseImpl.inList("s", "s");
		
		assertFalse(result);
		
		verify(blackListDao, times(1)).findByPersonFirstNameAndPersonSurName(anyString(), anyString());
		verifyNoMoreInteractions(blackListDao);
	}
	
	@Test
	public void givenPersonInBlackList_WhenRunInList_ThenReturnTrue() {
		when(blackListDao.findByPersonFirstNameAndPersonSurName(anyString(), anyString())).thenReturn(new BlackList());
		
		boolean result = personBlackListServiceDatabaseImpl.inList("s", "s");
		
		assertTrue(result);
		
		verify(blackListDao, times(1)).findByPersonFirstNameAndPersonSurName(anyString(), anyString());
		verifyNoMoreInteractions(blackListDao);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenBlackListDaoFails_WhenRunInList_ThenThrowException() {
		when(blackListDao.findByPersonFirstNameAndPersonSurName(anyString(), anyString())).thenThrow(Exception.class);
		
		personBlackListServiceDatabaseImpl.inList("s", "s");
	}
}
