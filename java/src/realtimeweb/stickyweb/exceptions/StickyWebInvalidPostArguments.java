package realtimeweb.stickyweb.exceptions;

/**
 * Thrown when the arguments for a Post call are invalid; most likely because an
 * incorrect character encoding.
 * 
 * @author acbart
 * 
 */
public class StickyWebInvalidPostArguments extends StickyWebException {

	public StickyWebInvalidPostArguments(String name) {
		super(name);
	}

}
