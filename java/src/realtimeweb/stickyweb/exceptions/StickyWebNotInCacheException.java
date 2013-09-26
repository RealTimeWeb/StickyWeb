package realtimeweb.stickyweb.exceptions;

public class StickyWebNotInCacheException extends Exception {

	public StickyWebNotInCacheException(String request) {
		super(request);
	}

}
