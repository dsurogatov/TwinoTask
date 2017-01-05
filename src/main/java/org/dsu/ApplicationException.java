/**
 * 
 */
package org.dsu;

import org.springframework.http.HttpStatus;

/** The appliction's level exception.
 *
 * @author nescafe
 */
public final class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -5296719837292805788L;
	
	public enum Type {
		PERSON_IN_BLACKLIST(HttpStatus.FORBIDDEN), LIMIT_REQUEST_REACHED(HttpStatus.FORBIDDEN);
		
		private final HttpStatus status;
		
		private Type(HttpStatus status) {
			this.status = status;
		}
	}
	
	private final Type type;
	
	public ApplicationException(Type type) {
		this.type = type;
	}

	/** Gets the type of the exception.
	 * 
	 * @return  The {@link ApplicationException.Type}
	 */
	public Type getType() {
		return type;
	}
	
	/** Gets HttpStatus for the exception's type.
	 * 
	 * @return
	 */
	public HttpStatus getStatus() {
		return getType().status;
	}

}
