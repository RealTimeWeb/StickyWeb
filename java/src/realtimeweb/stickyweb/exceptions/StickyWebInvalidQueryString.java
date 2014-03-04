package realtimeweb.stickyweb.exceptions;

/**
 * Thrown when the Query String of a Get request is invalid, usually because of
 * the URL or the parameters.
 * 
 * @author acbart
 * 
 */
public class StickyWebInvalidQueryString extends StickyWebException {

	public StickyWebInvalidQueryString(String name) {
		super(name);
	}

}
