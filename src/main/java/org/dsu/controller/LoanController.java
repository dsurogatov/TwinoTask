/**
 * 
 */
package org.dsu.controller;

import static org.dsu.Constant.API;
import static org.dsu.Constant.LOAN;
import static org.dsu.Constant.V1;

import java.util.List;

import javax.validation.Valid;

import org.dsu.dto.ApplyLoanDTO;
import org.dsu.dto.LoanDTO;
import org.dsu.service.loan.ApplyLoanService;
import org.dsu.service.loan.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller processes requests working with loans.
 * 
 * @author nescafe
 */
@RestController
@RequestMapping(API + "/" + V1 + "/" + LOAN)
public final class LoanController {

	//private static final Logger LOG = LoggerFactory.getLogger(LoanController.class);

	@Autowired
	private LoanService loanService;

	@Autowired
	private ApplyLoanService applyLoanService;

	@RequestMapping(value = "/approved", method = RequestMethod.GET)
	public List<LoanDTO> getApprovedLoans(Pageable page) {
		return loanService.findApprovedLoans(page);
	}

	@RequestMapping(value = "/approved/person", method = RequestMethod.GET)
	public List<LoanDTO> getApprovedLoansByPersonId(@RequestParam(value = "id", required = true) Long personId, Pageable page) {
		return loanService.findApprovedLoansByPersonId(personId, page);
	}

	@RequestMapping(method = RequestMethod.POST)
	public LoanDTO applyLoan(@Valid @RequestBody ApplyLoanDTO dto) {
		//LOG.info("dto - " + dto);
		return applyLoanService.apply(dto);
	}
}
