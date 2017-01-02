/**
 * 
 */
package org.dsu;

/** The appliction's level exception.
 *
 * @author nescafe
 */
public final class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -5296719837292805788L;
	
	public enum Type {
		PERSON_IN_BLACKLIST
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

}
