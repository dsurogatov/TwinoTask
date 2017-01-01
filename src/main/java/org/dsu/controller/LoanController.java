/**
 * 
 */
package org.dsu.controller;

import static org.dsu.Constant.API;
import static org.dsu.Constant.LOAN;
import static org.dsu.Constant.V1;

import java.util.List;

import org.dsu.dto.LoanDTO;
import org.dsu.service.loan.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The controller processes requests working with loans.
 * 
 * @author nescafe
 */
@RestController
@RequestMapping(API + "/" + V1 + "/" + LOAN)
public final class LoanController {
	
	//private static final Logger LOG = LoggerFactory.getLogger(LoanController.class);
	
	@Autowired
	private LoanService loanService;

	@RequestMapping(value="/approved", method=RequestMethod.GET)
	public List<LoanDTO> getApprovedLoansByPersonId(@RequestParam(value = "personId", required = false) Long personId, 
			Pageable page) {
//		LOG.info("Page - " + page);
//		LOG.info("Person - " + personId);
		if(personId == null) {
			return loanService.findApprovedLoans(page);
		} else {
			return loanService.findApprovedLoansByPersonId(personId, page);
		}
	}
}
