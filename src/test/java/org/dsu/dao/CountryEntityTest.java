/**
 * 
 */
package org.dsu.dao;

import javax.transaction.Transactional;

import org.dsu.config.DataSourceConfig;
import org.dsu.config.JPAConfig;
import org.dsu.domain.Country;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Checks database constraints of the {@link Country} entity.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class, JPAConfig.class, DataSourceConfig.class })
@Transactional
@Rollback
@ActiveProfiles("test")
public class CountryEntityTest {

	@Autowired
	private CountryDAO countryDao;
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenNullCode_WhenSaveCountry_ThenThrowException() {
		Country country = new Country();
		country.setCode(null);
		countryDao.saveAndFlush(country);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenLongLengthCode_WhenSaveCountry_ThenThrowException() {
		Country country = new Country();
		country.setCode("123");
		countryDao.saveAndFlush(country);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenTwoContriesWithSameCodes_WhenSaveCountry_ThenThrowException() {
		Country country = new Country();
		country.setCode("ru");
		countryDao.saveAndFlush(country);
		
		Country country1 = new Country();
		country1.setCode("ru");
		countryDao.saveAndFlush(country1);
	}
}
