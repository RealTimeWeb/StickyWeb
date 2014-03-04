package realtimeweb.stickyweb.exceptions;

/**
 * Thrown when the data source cannot be loaded; this might occur if the
 * filestream is not valid.
 * 
 * @author acbart
 * 
 */
public class StickyWebLoadDataSourceException extends StickyWebException {

	public StickyWebLoadDataSourceException(String message) {
		super(message);
	}

}
