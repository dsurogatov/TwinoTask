/**
 * 
 */
package org.dsu.controller;

import org.springframework.http.HttpStatus;
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
	
	//private static final Logger LOG = LoggerFactory.getLogger(RestExceptionProcessor.class);

	/** Contains an error description.
	 *  It is returned to client then an exception is thrown.
	 * 
	 * @author nescafe
	 */
	public static class ErrorInfo {
		private final String message;

		public ErrorInfo(String message) {
			this.message = message;
		}

		/** Gets the error message.
		 * 
		 * @return    The String value.
		 */
		public String getMessage() {
			return message;
		}
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
	public ErrorInfo handleAllException(Exception ex) {
		return new ErrorInfo("Internal server error.");
	}
}
