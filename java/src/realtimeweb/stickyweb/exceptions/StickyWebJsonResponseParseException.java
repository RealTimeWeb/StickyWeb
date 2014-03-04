package realtimeweb.stickyweb.exceptions;

/**
 * Thrown when the JSON response is from a call is invalid. (possibly returned
 * an HTML or Text formatted errors instead?).
 * 
 * @author acbart
 * 
 */
public class StickyWebJsonResponseParseException extends StickyWebException {
	public StickyWebJsonResponseParseException(String request) {
		super(request);
	}
}
