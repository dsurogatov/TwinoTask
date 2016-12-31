package org.dsu.dao;

import static org.dsu.TestObjectHelper.PAGE_DEFAULT;
import static org.dsu.TestObjectHelper.PERSON_DEFAULT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.dsu.config.DataSourceConfig;
import org.dsu.config.JPAConfig;
import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class, JPAConfig.class, DataSourceConfig.class })
@ActiveProfiles("test")
public class LoanDAOTest {

	@Autowired
	private LoanDAO loanDao;

	@Autowired
	private PersonDAO personDao;

	@Before
	public void cleanData() {
		loanDao.deleteAllInBatch();
		personDao.deleteAllInBatch();
	}

	@Test
	public void givenApprovedLoan_WhenFindApprovedLoans_ThenReturnLoan() {
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
			loan.setPerson(personDao.save(PERSON_DEFAULT));
			createdLoans.add(loan);
		}

		loanDao.save(createdLoans);

		List<Loan> result = loanDao.findByStatus(LoanStatus.APPROVED, PAGE_DEFAULT);

		assertEquals(PAGE_DEFAULT.getPageSize(), result.size());
	}

}
