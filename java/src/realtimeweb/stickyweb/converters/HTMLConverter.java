package realtimeweb.stickyweb.converters;

import java.io.IOException;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.xml.sax.SAXException;

public class HTMLConverter {
	
	private static HtmlCleaner cleaner = new HtmlCleaner();
	
	public static TagNode convert(String data) throws SAXException, IOException {
		return cleaner.clean(data);
	}
}
