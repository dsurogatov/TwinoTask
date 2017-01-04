/**
 * 
 */
package org.dsu.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.dsu.config.DataSourceConfig;
import org.dsu.config.JPAConfig;
import org.dsu.domain.Country;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Tests the {@link CountryDAO}.
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
public class CountryDAOTest {

	@Autowired
	private CountryDAO countryDao;

	@After
	public void cleanData() {
		countryDao.deleteAllInBatch();
	}
	
	@Test
	public void givenCountry_WhenFindByCode_ThenReturnCountry () {
		Country country = new Country();
		country.setCode("ru");
		countryDao.save(country);
		
		Country foundCountry = countryDao.findByCode("ru");

		assertNotNull(foundCountry.getId());
		assertEquals("ru", foundCountry.getCode());
	}
}
