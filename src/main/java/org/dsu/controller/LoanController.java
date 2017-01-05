/**
 * 
 */
package org.dsu.controller;

import static org.dsu.Constant.API;
import static org.dsu.Constant.LOAN;
import static org.dsu.Constant.V1;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.dsu.ApplicationException;
import org.dsu.dto.ApplyLoanDTO;
import org.dsu.dto.LoanDTO;
import org.dsu.dto.PersonDTO;
import org.dsu.service.countryresolver.CountryResolverService;
import org.dsu.service.loan.LoanApplicationService;
import org.dsu.service.loan.LoanService;
import org.dsu.service.validation.LoanApplicationLimitRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller processes requests working with loans.
 * 
 * @author nescafe
 */
@RestController
@RequestMapping(API + "/" + V1 + "/" + LOAN)
public final class LoanController {

//	private static final Logger LOG = LoggerFactory.getLogger(LoanController.class);

	@Autowired
	private LoanService loanService;

	@Autowired
	private LoanApplicationService loanApplicationService;

	@Autowired
	private CountryResolverService countryResolverService;
	
	@Autowired
	private LoanApplicationLimitRequestService loanApplicationLimitRequestService;

	@RequestMapping(value = "/approved", method = RequestMethod.GET)
	public List<LoanDTO> getApprovedLoans(Pageable page) {
		return loanService.findApprovedLoans(page);
	}

	@RequestMapping(value = "/approved/person/{personId}", method = RequestMethod.GET)
	public List<LoanDTO> getApprovedLoansByPersonId(@PathVariable Long personId, Pageable page) {
		return loanService.findApprovedLoansByPersonId(personId, page);
	}

	@RequestMapping(method = RequestMethod.POST)
	public LoanDTO applyLoan(@Valid @RequestBody ApplyLoanDTO dto) {
		//LOG.info("dto - " + dto);
		String countryCode = countryResolverService.resolveCode();
		if(StringUtils.isBlank(countryCode)) {
			throw new IllegalStateException("The country code has not been resolved.");
		}
		
		if(loanApplicationLimitRequestService.limitReachedByCountry(countryCode)) {
			throw new ApplicationException(ApplicationException.Type.LIMIT_REQUEST_REACHED);
		}
		
		LoanDTO loanDto = new LoanDTO(null, dto.getAmount(), dto.getTerm(), 
				new PersonDTO(null, dto.getFirstName(), dto.getSurName()), null, null, countryCode);
		return loanApplicationService.apply(loanDto);
	}
}
