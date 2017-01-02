/**
 * 
 */
package org.dsu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Intercepts and processes exceptions at controller level.
 * 
 * @author nescafe
 */
@ControllerAdvice
public final class RestExceptionProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(RestExceptionProcessor.class);

	/**
	 * Contains an error description. It is returned to client then an exception is thrown.
	 * 
	 * @author nescafe
	 */
	public static class ErrorInfo {
		private final String message;
		private final List<String[]> validationMessages = new ArrayList<String[]>();

		public ErrorInfo(String message) {
			this.message = message;
		}

		/**
		 * Gets the error message.
		 * 
		 * @return The String value.
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * Adds the validation error.
		 * 
		 * @param field The name of field.
		 * @param message The error message.
		 */
		public void addFieldError(String field, String message) {
			validationMessages.add(new String[] { field, message });
		}

		/**
		 * Gets error messages by field.
		 * 
		 * @return The list of {field, message} values.
		 */
		public List<String[]> getValidationMessages() {
			return validationMessages;
		}
	}

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorInfo handleAllException(Exception ex) {
		LOG.error(ex.getMessage(), ex);
		return new ErrorInfo("Internal server error.");
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorInfo handleMissingServletRequestParameterException(Exception ex) {
		return new ErrorInfo(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorInfo handleValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();

		return processFieldErrors(new ErrorInfo("The validation error."), fieldErrors);
	}

	private ErrorInfo processFieldErrors(final ErrorInfo dto, List<FieldError> fieldErrors) {
		fieldErrors.forEach(f -> {
			String localizedErrorMessage = resolveLocalizedErrorMessage(f);
			dto.addFieldError(f.getField(), localizedErrorMessage);
		});

		return dto;
	}

	private String resolveLocalizedErrorMessage(FieldError fieldError) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);

		if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
			localizedErrorMessage = messageSource.getMessage(fieldError.getDefaultMessage(), fieldError.getArguments(),
			        currentLocale);
		}

		return localizedErrorMessage;
	}
}
