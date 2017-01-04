/**
 * 
 */
package org.dsu.service.loan;

import static org.dsu.Constant.MESSAGE_ARG_MUSNT_BE_NULL;
import static org.dsu.Constant.MESSAGE_PAGESIZE_MORE_THAN_MAX;
import static org.dsu.Constant.PAGE_MAX_SIZE;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.dsu.dao.LoanDAO;
import org.dsu.dao.PersonDAO;
import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.dsu.domain.Person;
import org.dsu.dto.LoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implements the LoanService interface.
 *
 * @author nescafe
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {

	private static final String[] PAGE_ARGS = new String[] {"page"};
	private static final String[] PERSONID_ARGS = new String[] {"personId"};

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LoanDAO loanDao;

	@Autowired
	private PersonDAO personDao;

	@Override
	public List<LoanDTO> findApprovedLoans(Pageable page) {
		Assert.notNull(page, messageSource.getMessage(MESSAGE_ARG_MUSNT_BE_NULL, PAGE_ARGS, Locale.ENGLISH));
		Assert.isTrue(page.getPageSize() <= PAGE_MAX_SIZE,
		        messageSource.getMessage(MESSAGE_PAGESIZE_MORE_THAN_MAX, PAGE_ARGS, Locale.ENGLISH));

		List<Loan> loans = loanDao.findByStatus(LoanStatus.APPROVED, page);
		return loans.stream().map(Converter::toLoanDTO).collect(Collectors.toList());
	}

	@Override
	public List<LoanDTO> findApprovedLoansByPersonId(Long personId, Pageable page) {
		Assert.notNull(page, messageSource.getMessage(MESSAGE_ARG_MUSNT_BE_NULL, PAGE_ARGS, Locale.ENGLISH));
		Assert.isTrue(page.getPageSize() <= PAGE_MAX_SIZE,
		        messageSource.getMessage(MESSAGE_PAGESIZE_MORE_THAN_MAX, PAGE_ARGS, Locale.ENGLISH));
		Assert.notNull(personId, messageSource.getMessage(MESSAGE_ARG_MUSNT_BE_NULL, PERSONID_ARGS, Locale.ENGLISH));

		Person person = personDao.findOne(personId);
		if(person == null) {
			return Collections.emptyList();
		}

		List<Loan> loans = loanDao.findByStatusAndPerson(LoanStatus.APPROVED, person, page);
		return loans.stream().map(Converter::toLoanDTO).collect(Collectors.toList());
	}

}
