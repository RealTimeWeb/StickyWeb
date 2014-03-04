package realtimeweb.stickyweb.exceptions;

/**
 * Thrown when the data source could not be parsed, usually because the JSON
 * structure is incorrect.
 * 
 * @author acbart
 * 
 */
public class StickyWebDataSourceParseException extends StickyWebException {

	public StickyWebDataSourceParseException(String request) {
		super(request);
	}

}
