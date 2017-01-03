/**
 * 
 */
package org.dsu;

/** Defines the application's constants.
 * 
 * @author nescafe
 */
public final class Constant {

	private Constant() {
		
	}
	
	public static final String API = "api";
	public static final String V1 = "v1";
	public static final String LOAN = "loan";
	
	public static final int PAGE_MAX_SIZE = 2000;
	
	public static final String MESSAGE_ARG_MUSNT_BE_NULL = "argument.must.not.be.null";
	public static final String MESSAGE_ARG_MUSNT_BE_NULL_OR_EMPTY = "argument.must.not.be.null.or.empty";
	public static final String MESSAGE_PAGESIZE_MORE_THAN_MAX = "pageSize.more.than.max";
	
	public static final String REQUEST_COUNTRY_CODE_ATTR_NAME = "org.dsu.country.code";
}
