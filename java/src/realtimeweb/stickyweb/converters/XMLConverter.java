package realtimeweb.stickyweb.converters;

import java.io.IOException;
import java.io.StringReader;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class XMLConverter {
	
	private static DOMParser parser = new DOMParser();

	public static Document convert(String data) throws SAXException, IOException {
		parser.parse(new InputSource(new StringReader(data)));
		Document doc = parser.getDocument();
		return doc;
	}

}
