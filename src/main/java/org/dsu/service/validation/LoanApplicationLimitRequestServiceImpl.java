/**
 * 
 */
package org.dsu.service.validation;

import static org.dsu.Constant.APP_PROP_LOANAPPLIMITREQUEST_TOO_MANY;
import static org.dsu.Constant.APP_PROP_LOANAPPLIMITREQUEST_TOO_MANY_COUNT;
import static org.dsu.Constant.APP_PROP_LOANAPPLIMITREQUEST_TOO_MANY_TIME_SEC;
import static org.dsu.Constant.APP_PROP_LOANAPPLIMITREQUEST_SECOND;
import static org.dsu.Constant.MESSAGE_ARG_MUSNT_BE_NULL_OR_EMPTY;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dsu.dao.LoanDAO;
import org.dsu.domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * Implements the {@link LoanApplicationLimitRequestService}.
 *
 * @author nescafe
 */
@Service
@Transactional
public class LoanApplicationLimitRequestServiceImpl implements LoanApplicationLimitRequestService {

	@Value("${" + APP_PROP_LOANAPPLIMITREQUEST_SECOND + ":false}")
	private boolean validateSecond;

	@Value("${" + APP_PROP_LOANAPPLIMITREQUEST_TOO_MANY + ":false}")
	private boolean validateTooMany;

	@Value("${" + APP_PROP_LOANAPPLIMITREQUEST_TOO_MANY_TIME_SEC + ":86400}")
	private int validateTooManyTimeFrame;

	@Value("${" + APP_PROP_LOANAPPLIMITREQUEST_TOO_MANY_COUNT + ":1000}")
	private int validateTooManyCount;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LoanDAO loanDao;

	@Override
	public boolean limitReachedByCountry(String countryCode) {
		if (StringUtils.isBlank(countryCode)) {
			throw new IllegalArgumentException(messageSource.getMessage(MESSAGE_ARG_MUSNT_BE_NULL_OR_EMPTY,
			        new String[] { "countryCode" }, Locale.ENGLISH));
		}

		return validateSecondRequest(countryCode.toLowerCase()) || validateNumberRequest(countryCode.toLowerCase());
	}

	private boolean validateSecondRequest(String countryCode) {
		if (validateSecond) {
			Loan loan = loanDao.findTopByOrderByCreatedDesc();
			if (loan != null && loan.getCountry() != null && countryCode.equals(loan.getCountry().getCode())) {
				return true;
			}
		}
		return false;
	}

	private boolean validateNumberRequest(String countryCode) {
		if (validateTooMany && validateTooManyTimeFrame > 0 && validateTooManyCount > 0) {
			LocalDateTime end = LocalDateTime.now();
			int count = loanDao.countByCountryCodeAndCreatedBetween(countryCode,
			        end.minus(validateTooManyTimeFrame, ChronoUnit.SECONDS), end);
			if (count >= validateTooManyCount) {
				return true;
			}
		}
		return false;
	}
}
