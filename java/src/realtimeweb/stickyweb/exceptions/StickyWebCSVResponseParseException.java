package realtimeweb.stickyweb.exceptions;

/**
 * Thrown when the structure of the returned CSV data is incorrect. (possibly
 * returned an HTML or Text formatted errors instead?).
 * 
 * @author acbart
 * 
 */
public class StickyWebCSVResponseParseException extends StickyWebException {

	public StickyWebCSVResponseParseException(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
