/**
 * 
 */
package org.dsu.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.dsu.dao.BlackListDAO;
import org.dsu.dao.PersonDAO;
import org.dsu.domain.BlackList;
import org.dsu.domain.Person;
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

	@Autowired
	private PersonDAO personDao;
	
	@Before
	public void setUp() {
		Mockito.reset(blackListDao);
		Mockito.reset(personDao);
	}
	
	@Test
	public void givenPersonNotInBlackList_WhenRunInList_ThenReturnFalse() {
		when(personDao.findOne(anyLong())).thenReturn(new Person());
		when(blackListDao.findByPerson(any(Person.class))).thenReturn(null);
		
		boolean result = personBlackListServiceDatabaseImpl.inList(1L);
		
		assertFalse(result);
		
		verify(personDao, times(1)).findOne(anyLong());
		verifyNoMoreInteractions(personDao);
		verify(blackListDao, times(1)).findByPerson(any(Person.class));
		verifyNoMoreInteractions(blackListDao);
	}
	
	@Test
	public void givenPersonInBlackList_WhenRunInList_ThenReturnTrue() {
		when(personDao.findOne(anyLong())).thenReturn(new Person());
		when(blackListDao.findByPerson(any(Person.class))).thenReturn(new BlackList());
		
		boolean result = personBlackListServiceDatabaseImpl.inList(1L);
		
		assertTrue(result);
		
		verify(personDao, times(1)).findOne(anyLong());
		verifyNoMoreInteractions(personDao);
		verify(blackListDao, times(1)).findByPerson(any(Person.class));
		verifyNoMoreInteractions(blackListDao);
	}
	
	@Test
	public void givenNotExistPerson_WhenRunInList_ThenReturnFalse() {
		when(personDao.findOne(anyLong())).thenReturn(null);
		
		boolean result = personBlackListServiceDatabaseImpl.inList(1L);
		
		assertFalse(result);
		
		verify(personDao, times(1)).findOne(anyLong());
		verifyNoMoreInteractions(personDao);
		verifyZeroInteractions(blackListDao);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenPersonDaoFails_WhenRunInList_ThenThrowException() {
		when(personDao.findOne(anyLong())).thenThrow(Exception.class);
		
		personBlackListServiceDatabaseImpl.inList(1L);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void givenBlackListDaoFails_WhenRunInList_ThenThrowException() {
		when(personDao.findOne(anyLong())).thenReturn(new Person());
		when(blackListDao.findByPerson(any(Person.class))).thenThrow(Exception.class);
		
		personBlackListServiceDatabaseImpl.inList(1L);
	}
}
