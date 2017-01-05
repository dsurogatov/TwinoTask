package org.dsu.dao;

import static org.dsu.TestObjectHelper.PAGE_DEFAULT;
import static org.dsu.TestObjectHelper.PERSON_DEFAULT;
import static org.dsu.TestObjectHelper.approvedLoan;
import static org.dsu.TestObjectHelper.country;
import static org.dsu.TestObjectHelper.person;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.dsu.config.DataSourceConfig;
import org.dsu.config.JPAConfig;
import org.dsu.domain.Country;
import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.dsu.domain.Person;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class, JPAConfig.class, DataSourceConfig.class })
@ActiveProfiles("test")
@TestPropertySource(locations = {
		"classpath:test-jdbc.properties",
		"classpath:test-hibernate.properties"
		})
public class LoanDAOTest {

	@Autowired
	private LoanDAO loanDao;

	@Autowired
	private PersonDAO personDao;

	@Autowired
	private CountryDAO countryDao;

	@After
	public void cleanData() {
		loanDao.deleteAllInBatch();
		personDao.deleteAllInBatch();
	}

	@Test
	public void givenApprovedLoan_WhenFindApprovedLoans_ThenReturnLoans() {
		Loan loan = new Loan();
		loan.setAmount(BigDecimal.valueOf(11.645));
		loan.setTerm("term");
		loan.setStatus(LoanStatus.APPROVED);
		loan.setPerson(personDao.save(PERSON_DEFAULT));

		loanDao.save(loan);

		List<Loan> result = loanDao.findByStatus(LoanStatus.APPROVED, PAGE_DEFAULT);

		assertEquals(1, result.size());
		Loan entity = result.get(0);
		assertNotNull(entity.getId());
		assertEquals(BigDecimal.valueOf(11.65), entity.getAmount());
		assertEquals("term", entity.getTerm());
		assertNotNull(entity.getCreated());
		assertNotNull(entity.getPerson().getId());
		assertEquals(PERSON_DEFAULT.getFirstName(), entity.getPerson().getFirstName());
		assertEquals(PERSON_DEFAULT.getSurName(), entity.getPerson().getSurName());
	}

	@Test
	public void givenLotsOfLoans_WhenFindLoansByStatus_ThenReturnPageSizeLoans() {
		List<Loan> createdLoans = new ArrayList<>(100);
		for (int i = 0; i < PAGE_DEFAULT.getPageSize() + 10; i++) {
			Loan loan = new Loan();
			loan.setAmount(BigDecimal.valueOf(11.645));
			loan.setTerm("term");
			loan.setStatus(LoanStatus.APPROVED);
			Person person = new Person();
			person.setFirstName("firstname" + i);
			person.setSurName("surName");
			loan.setPerson(personDao.save(person));
			createdLoans.add(loan);
		}

		loanDao.save(createdLoans);

		List<Loan> result = loanDao.findByStatus(LoanStatus.APPROVED, PAGE_DEFAULT);

		assertEquals(PAGE_DEFAULT.getPageSize(), result.size());
	}
	
	@Test
	public void givenApprovedLoan_WhenFindApprovedLoansByPerson_ThenReturnLoans() {
		Loan loan = approvedLoan(null, 11.645, "term", personDao.save(PERSON_DEFAULT), null);
		loanDao.save(loan);

		List<Loan> result = loanDao.findByStatusAndPerson(LoanStatus.APPROVED, loan.getPerson(), PAGE_DEFAULT);

		assertEquals(1, result.size());
		Loan entity = result.get(0);
		assertNotNull(entity.getId());
		assertEquals(BigDecimal.valueOf(11.65), entity.getAmount());
		assertEquals("term", entity.getTerm());
		assertNotNull(entity.getCreated());
		assertNotNull(entity.getPerson().getId());
		assertEquals(PERSON_DEFAULT.getFirstName(), entity.getPerson().getFirstName());
		assertEquals(PERSON_DEFAULT.getSurName(), entity.getPerson().getSurName());
		assertNull(entity.getCountry());
	}
	
	@Test
	public void whenFindTopByOrderByCreatedDesc_ThenReturnLastAddedLoan() {
		Loan loan = approvedLoan(null, 11.645, "term", personDao.save(PERSON_DEFAULT), null);
		loanDao.save(loan);
		loan = approvedLoan(null, 10.645, "term1", personDao.save(person(null, "first1", "sur1")), null);
		loanDao.save(loan);
		
		Loan foundLoan = loanDao.findTopByOrderByCreatedDesc();
		assertNotNull(foundLoan.getId());
		assertEquals(BigDecimal.valueOf(10.65), foundLoan.getAmount());
		assertEquals("term1", foundLoan.getTerm());
		assertNotNull(foundLoan.getCreated());
		assertNotNull(foundLoan.getPerson().getId());
		assertEquals("first1", foundLoan.getPerson().getFirstName());
		assertEquals("sur1", foundLoan.getPerson().getSurName());
		assertNull(foundLoan.getCountry());
	}
	
	@Test
	public void whenCountByCountryCodeAndCreatedBetween_ThenReturnOne() {
		Country ru = countryDao.save(country(null, "ru"));
		Loan loan = approvedLoan(null, 1, "term", personDao.save(PERSON_DEFAULT), ru);
		loanDao.save(loan);
		
		int result = loanDao.countByCountryCodeAndCreatedBetween("ru", 
				LocalDateTime.now().minus(30, ChronoUnit.SECONDS), LocalDateTime.now());
		assertEquals(1, result);
	}

}
