package realtimeweb.stickyweb;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.TagNode;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import realtimeweb.stickyweb.converters.HTMLConverter;
import realtimeweb.stickyweb.converters.JsonConverter;
import realtimeweb.stickyweb.converters.XMLConverter;
import realtimeweb.stickyweb.exceptions.StickyWebCSVResponseParseException;
import realtimeweb.stickyweb.exceptions.StickyWebJsonResponseParseException;
import realtimeweb.stickyweb.exceptions.StickyWebXMLResponseParseException;
import au.com.bytecode.opencsv.CSVReader;

public class StickyWebResponse {

	private String text;

	/**
	 * Create a new StickyWebReponse from raw data.
	 * 
	 * @param text
	 */
	public StickyWebResponse(String text) {
		this.text = text;
	}

	/**
	 * Identifies whether or not the result is a null value (which is returned
	 * by the cache when it's empty)
	 * 
	 * @return
	 */
	public boolean isNull() {
		return this.text == null;
	}

	/**
	 * Occasionally, data will come in malformed for one reason or another. This
	 * function, in conjunction with asText, allows you to manipulate the data
	 * before it is returned through asJSON or another method.
	 * 
	 * @param newText
	 */
	public void setText(String newText) {
		this.text = newText;
	}

	/**
	 * Return the StickyWebResponse data as raw text.
	 * 
	 * @return
	 */
	public String asText() {
		return text;
	}

	/**
	 * Return the data as structured JSON data, encoded in a Map.
	 * 
	 * @return
	 * @throws ParseException
	 * @throws StickyWebJsonResponseParseException
	 */
	public Map<String, Object> asJSON()
			throws StickyWebJsonResponseParseException {
		return JsonConverter.convertToMap(text);
	}

	/**
	 * Return the data as structured JSON data, encoded in a List.
	 * 
	 * @return
	 * @throws ParseException
	 */
	public List<Object> asJSONArray()
			throws StickyWebJsonResponseParseException {
		return JsonConverter.convertToList(text);
	}

	public static String jsonToText(Object obj) {
		return JSONValue.toJSONString(obj);
	}

	/**
	 * Return the data as CSV, which will be a list of arrays of strings.
	 * 
	 * @return
	 * @throws StickyWebCSVResponseParseException
	 */
	public List<String[]> asCSV() throws StickyWebCSVResponseParseException {
		try {
			CSVReader reader = new CSVReader(new StringReader(text));
			List<String[]> result;
			result = reader.readAll();
			reader.close();
			return result;
		} catch (IOException e) {
			throw new StickyWebCSVResponseParseException(e.getMessage());
		}
	}

	/**
	 * Return the data as CSV, which will be a list of arrays of strings.
	 * Features options for parsing the CSV.
	 * 
	 * @param separator
	 * @param quotechar
	 * @param escape
	 * @return
	 * @throws StickyWebCSVResponseParseException
	 */
	public List<String[]> asCSV(char separator, char quotechar, char escape)
			throws StickyWebCSVResponseParseException {
		try {
			CSVReader reader = new CSVReader(new StringReader(text), separator,
					quotechar, escape);
			List<String[]> result = reader.readAll();
			reader.close();
			return result;
		} catch (IOException e) {
			throw new StickyWebCSVResponseParseException(e.getMessage());
		}
	}

	/**
	 * Returns the text as an XML document, which can then be queried using the
	 * native <a href=
	 * 'http://docs.oracle.com/javase/tutorial/jaxp/xslt/xpath.html'>XPath</a>.
	 * 
	 * @return
	 * @throws StickyWebXMLResponseParseException
	 */
	public Document asXML() throws StickyWebXMLResponseParseException {
		try {
			return XMLConverter.convert(text);
		} catch (SAXException e) {
			throw new StickyWebXMLResponseParseException(e.getMessage());
		} catch (IOException e) {
			throw new StickyWebXMLResponseParseException(e.getMessage());
		}
	}

	/**
	 * Returns the text as an HTML document, which can then be queried using the
	 * method evaluateXPath, or any of the methods related to the <a href=
	 * 'http://htmlcleaner.sourceforge.net/doc/index.html'>htmlcleaner</a>
	 * library.
	 * 
	 * @return
	 * @throws StickyWebXMLResponseParseException 
	 */
	public TagNode asHTML() throws StickyWebXMLResponseParseException {
		try {
			return HTMLConverter.convert(text);
		} catch (SAXException e) {
			throw new StickyWebXMLResponseParseException(e.getMessage());
		} catch (IOException e) {
			throw new StickyWebXMLResponseParseException(e.getMessage());
		}
	}

}
