/**
 * 
 */
package org.dsu.service.loan;

import static org.dsu.Constant.MESSAGE_ARG_MUSNT_BE_NULL;
import static org.dsu.Constant.MESSAGE_ARG_MUSNT_BE_NULL_OR_EMPTY;

import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dsu.dao.LoanDAO;
import org.dsu.dao.PersonDAO;
import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.dsu.domain.Person;
import org.dsu.dto.ApplyLoanDTO;
import org.dsu.dto.LoanDTO;
import org.dsu.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implements {@link ApplyLoanService}
 *
 * @author nescafe
 */
@Service
@Transactional
public class ApplyLoanServiceImpl implements ApplyLoanService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LoanDAO loanDao;

	@Autowired
	private PersonDAO personDao;

	@Override
	public LoanDTO apply(ApplyLoanDTO applyDto) {
		Assert.notNull(applyDto,
		        messageSource.getMessage(MESSAGE_ARG_MUSNT_BE_NULL, new String[] { "applyDto" }, Locale.ENGLISH));
		if (StringUtils.isBlank(applyDto.getFirstName())) {
			throw new IllegalArgumentException(messageSource.getMessage(MESSAGE_ARG_MUSNT_BE_NULL_OR_EMPTY,
			        new String[] { "applyDto#firstName" }, Locale.ENGLISH));
		}
		if (StringUtils.isBlank(applyDto.getSurName())) {
			throw new IllegalArgumentException(messageSource.getMessage(MESSAGE_ARG_MUSNT_BE_NULL_OR_EMPTY,
			        new String[] { "applyDto#surName" }, Locale.ENGLISH));
		}

		String firstName = applyDto.getFirstName().trim();
		String surName = applyDto.getSurName().trim();
		Person person = personDao.findByFirstNameAndSurName(firstName, surName);
		if (person == null) {
			Person createdPerson = new Person();
			createdPerson.setFirstName(firstName);
			createdPerson.setSurName(surName);
			person = personDao.save(createdPerson);
		}

		Loan loan = new Loan();
		loan.setAmount(applyDto.getAmount());
		loan.setTerm(applyDto.getTerm());
		loan.setPerson(person);
		loan.setStatus(LoanStatus.APPROVED);
		loan = loanDao.save(loan);

		return new LoanDTO(loan.getId(), loan.getAmount(), loan.getTerm(),
		        new PersonDTO(loan.getPerson().getId(), loan.getPerson().getFirstName(), loan.getPerson().getSurName()),
		        loan.getCreated(), loan.getStatus().name().toLowerCase());
	}

}
