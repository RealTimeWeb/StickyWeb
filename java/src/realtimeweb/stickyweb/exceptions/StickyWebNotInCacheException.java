package realtimeweb.stickyweb.exceptions;

/**
 * The given query was not available in the cache. Possibly you meant another
 * query, but mistyped one of the arguments.
 * 
 * @author acbart
 * 
 */
public class StickyWebNotInCacheException extends StickyWebException {

	public StickyWebNotInCacheException(String request) {
		super(request);
	}

}
