/**
 * 
 */
package org.dsu;

/**
 * Defines the application's constants.
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

	public static final String APP_PROP_IPINFO_URL = "ipinfo.url";
	public static final String APP_PROP_IPINFO_TIMEOUT = "ipinfo.timeout";
	public static final String APP_PROP_REMOTEADDRESSRESOLVER_SERVICE = "RemoteAddressResolver.name";
	public static final String APP_PROP_LOANAPPLIMITREQUEST_SECOND = "LoanApplicationLimitRequest.validate.second";
	public static final String APP_PROP_LOANAPPLIMITREQUEST_TOO_MANY = "LoanApplicationLimitRequest.validate.too.many";
	public static final String APP_PROP_LOANAPPLIMITREQUEST_TOO_MANY_TIME_SEC = "LoanApplicationLimitRequest.too.many.timeframe.sec";
	public static final String APP_PROP_LOANAPPLIMITREQUEST_TOO_MANY_COUNT = "LoanApplicationLimitRequest.too.many.count";
}
