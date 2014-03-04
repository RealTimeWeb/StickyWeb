package realtimeweb.stickyweb.exceptions;

/**
 * Thrown when the given data source (i.e., the InputSteam) is not found.
 * Usually, this means that the InputStream was null.
 * 
 * @author acbart
 * 
 */
public class StickyWebDataSourceNotFoundException extends StickyWebException {

	public StickyWebDataSourceNotFoundException(String name) {
		super(name);
	}

}
