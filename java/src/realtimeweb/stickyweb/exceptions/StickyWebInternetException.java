package realtimeweb.stickyweb.exceptions;

/**
 * Thrown when the internet cannot be accessed, typically because the network is
 * down.
 * 
 * @author acbart
 * 
 */
public class StickyWebInternetException extends StickyWebException {

	public StickyWebInternetException(String message) {
		super(message);
	}

}
