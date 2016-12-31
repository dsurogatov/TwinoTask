/**
 * 
 */
package org.dsu.service.loan;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.dsu.Constant;
import org.dsu.dao.LoanDAO;
import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.dsu.domain.Person;
import org.dsu.dto.LoanDTO;
import org.dsu.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/** Implements the LoanService interface.
 *
 * @author nescafe
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {
	
	private static LoanDTO toDTO(Loan loan) {
		Person loanPerson = loan.getPerson();
		PersonDTO loanPersonDTO = new PersonDTO(loanPerson.getId(), loanPerson.getFirstName(), loanPerson.getSurName()); 
		return new LoanDTO(loan.getId(), loan.getAmount(), loan.getTerm(), loanPersonDTO, loan.getCreated());
	}
	
	@Autowired
	private LoanDAO loanDao;

	@Override
	public List<LoanDTO> findAllApproved(Pageable page) {
		Assert.notNull(page, "The argument 'page' must not be null.");
		Assert.isTrue(page.getPageSize() <= Constant.PAGE_MAX_SIZE, "The value 'pageSize' of the argumen 'page' is more than max size.");
		
		List<Loan> loans = loanDao.findByStatus(LoanStatus.APPROVED, page);
		return loans.stream().map(LoanServiceImpl::toDTO).collect(Collectors.toList());
	}

}
