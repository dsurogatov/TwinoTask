/**
 * 
 */
package org.dsu.dao;

import static org.dsu.TestObjectHelper.PERSON_DEFAULT;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.dsu.config.DataSourceConfig;
import org.dsu.config.JPAConfig;
import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Test checks database constraints the {@link Loan} entity.
 *
 * @author nescafe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class, JPAConfig.class, DataSourceConfig.class })
@Transactional
@Rollback
@ActiveProfiles("test")
public class LoanEntityTest {
	
	@Autowired
	private LoanDAO loanDao;
	
	@Autowired
	private PersonDAO personDao;
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenNullAmount_WhenSaveLoan_ThenThrowException() {
		Loan loan = new Loan();
		//loan.setAmount(BigDecimal.valueOf(11.645));
		loan.setTerm("term");
		loan.setStatus(LoanStatus.APPROVED);
		loan.setPerson(personDao.save(PERSON_DEFAULT));
		
		loanDao.saveAndFlush(loan);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenNullTerm_WhenSaveLoan_ThenThrowException() {
		Loan loan = new Loan();
		loan.setAmount(BigDecimal.valueOf(11.645));
		//loan.setTerm("term");
		loan.setStatus(LoanStatus.APPROVED);
		loan.setPerson(personDao.save(PERSON_DEFAULT));
		
		loanDao.saveAndFlush(loan);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenNullStatus_WhenSaveLoan_ThenThrowException() {
		Loan loan = new Loan();
		loan.setAmount(BigDecimal.valueOf(11.645));
		loan.setTerm("term");
		loan.setStatus(null);
		loan.setPerson(personDao.save(PERSON_DEFAULT));
		
		loanDao.saveAndFlush(loan);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenNullPerson_WhenSaveLoan_ThenThrowException() {
		Loan loan = new Loan();
		loan.setAmount(BigDecimal.valueOf(11.645));
		loan.setTerm("term");
		loan.setStatus(LoanStatus.APPROVED);
		//loan.setPerson(personDao.save(PERSON_DEFAULT));
		
		loanDao.saveAndFlush(loan);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenLengthyTerm_WhenSaveLoan_ThenThrowException() {
		Loan loan = new Loan();
		loan.setAmount(BigDecimal.valueOf(11.645));
		loan.setTerm(RandomStringUtils.random(10001));
		loan.setStatus(LoanStatus.APPROVED);
		loan.setPerson(personDao.save(PERSON_DEFAULT));
		
		loanDao.saveAndFlush(loan);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void givenBigAmount_WhenSaveLoan_ThenThrowException() {
		Loan loan = new Loan();
		loan.setAmount(BigDecimal.valueOf(123456789));
		loan.setTerm("term");
		loan.setStatus(LoanStatus.APPROVED);
		loan.setPerson(personDao.save(PERSON_DEFAULT));
		
		loanDao.saveAndFlush(loan);
	}
	
}
