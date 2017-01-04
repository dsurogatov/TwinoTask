/**
 * 
 */
package org.dsu.service.loan;

import static org.dsu.Constant.MESSAGE_ARG_MUSNT_BE_NULL;
import static org.dsu.Constant.MESSAGE_ARG_MUSNT_BE_NULL_OR_EMPTY;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.dsu.ApplicationException;
import org.dsu.dao.CountryDAO;
import org.dsu.dao.LoanDAO;
import org.dsu.dao.PersonDAO;
import org.dsu.domain.Country;
import org.dsu.domain.Loan;
import org.dsu.domain.LoanStatus;
import org.dsu.domain.Person;
import org.dsu.dto.LoanDTO;
import org.dsu.service.blacklist.PersonBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implements {@link LoanApplyService}
 *
 * @author nescafe
 */
@Service
public class LoanApplyServiceImpl implements LoanApplyService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LoanDAO loanDao;

	@Autowired
	private PersonDAO personDao;

	@Autowired
	private CountryDAO countryDao;
	
	@Autowired
	private PersonBlackListService personBlackListService;

	@Override
	public LoanDTO apply(LoanDTO applyDto) {
		validateInputLoan(applyDto);

		String firstName = applyDto.getPersonDto().getFirstName().trim();
		String surName = applyDto.getPersonDto().getSurName().trim();
		
		Person person = findOrCreatePerson(firstName, surName);

		Loan loan = new Loan();
		loan.setAmount(applyDto.getAmount());
		loan.setTerm(applyDto.getTerm());
		loan.setPerson(person);
		loan.setStatus(LoanStatus.APPROVED);
		
		Country country = findOrCreateCountry(applyDto);
		loan.setCountry(country);
		
		loan = loanDao.save(loan);

		return Converter.toLoanDTO(loan);
	}

	private void validateInputLoan(LoanDTO applyDto) {
		Assert.notNull(applyDto,
		        messageSource.getMessage(MESSAGE_ARG_MUSNT_BE_NULL, new String[] { "applyDto" }, Locale.ENGLISH));
		Assert.notNull(applyDto.getPersonDto(),
		        messageSource.getMessage(MESSAGE_ARG_MUSNT_BE_NULL, new String[] { "applyDto#personDto" }, Locale.ENGLISH));
		if (StringUtils.isBlank(applyDto.getPersonDto().getFirstName())) {
			throw new IllegalArgumentException(messageSource.getMessage(MESSAGE_ARG_MUSNT_BE_NULL_OR_EMPTY,
			        new String[] { "applyDto#personDto#firstName" }, Locale.ENGLISH));
		}
		if (StringUtils.isBlank(applyDto.getPersonDto().getSurName())) {
			throw new IllegalArgumentException(messageSource.getMessage(MESSAGE_ARG_MUSNT_BE_NULL_OR_EMPTY,
			        new String[] { "applyDto#personDto#surName" }, Locale.ENGLISH));
		}
	}

	private Country findOrCreateCountry(LoanDTO applyDto) {
		if(StringUtils.isBlank(applyDto.getCountryCode())) {
			return null;
		}
		
		String countryCode = applyDto.getCountryCode().trim().toLowerCase();
		Country country = countryDao.findByCode(countryCode);
		if(country == null) {
			Country createdCountry = new Country();
			createdCountry.setCode(countryCode);
			
			// Might someone else already save the country?
			try {
				country = countryDao.save(createdCountry);
			} catch (DataIntegrityViolationException e) {
				country = countryDao.findByCode(countryCode);
				if(country == null) {
					throw new IllegalStateException("The CountryDAO#save has failed.", e);
				}
			}
		}
		return country;
	}

	private Person findOrCreatePerson(String firstName, String surName) {
		Person person = personDao.findByFirstNameAndSurName(firstName, surName);
		if (person == null) {
			Person createdPerson = new Person();
			createdPerson.setFirstName(firstName);
			createdPerson.setSurName(surName);
			
			try {
				person = personDao.save(createdPerson);
			} catch (DataIntegrityViolationException e) {
				person = personDao.findByFirstNameAndSurName(firstName, surName);
				if(person == null) {
					throw new IllegalStateException("The PersonDAO#save has failed.", e);
				}
			}
		} else if (personBlackListService.inList(person.getId())) {
			throw new ApplicationException(ApplicationException.Type.PERSON_IN_BLACKLIST);
		}
		return person;
	}

}
