package realtimeweb.stickyweb;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.TagNode;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import realtimeweb.stickyweb.converters.HTMLConverter;
import realtimeweb.stickyweb.converters.JsonConverter;
import realtimeweb.stickyweb.converters.XMLConverter;
import realtimeweb.stickyweb.exceptions.StickyWebJsonResponseParseException;

import au.com.bytecode.opencsv.CSVReader;

public class StickyWebResponse {
	
	private String text;

	/**
	 * Create a new StickyWebReponse from raw data.
	 * @param text
	 */
	public StickyWebResponse(String text) {
		this.text = text;
	}
	
	/**
	 * Occasionally, data will come in malformed for one reason or another.
	 * This function, in conjunction with asText, allows you to manipulate the data
	 * before it is returned through asJSON or another method.
	 * @param newText
	 */
	public void setText(String newText) {
		this.text = newText;
	}

	/**
	 * Return the StickyWebResponse data as raw text.
	 * @return
	 */
	public String asText() {
		return text;
	}
	
	/**
	 * Return the data as structured JSON data, encoded in a Map.
	 * @return
	 * @throws ParseException
	 * @throws StickyWebJsonResponseParseException 
	 */
	public Map<String, Object> asJSON() throws StickyWebJsonResponseParseException {
		return JsonConverter.convertToMap(text);
	}
	
	/**
	 * Return the data as structured JSON data, encoded in a List.
	 * @return
	 * @throws ParseException
	 */
	public List<Object> asJSONArray() throws StickyWebJsonResponseParseException {
		return JsonConverter.convertToList(text);
	}
	
	/**
	 * Return the data as CSV, which will be a list of arrays of strings.
	 * @return
	 * @throws IOException
	 */
	public List<String[]> asCSV() throws IOException {
		CSVReader reader = new CSVReader(new StringReader(text));
		List<String[]> result = reader.readAll();
		reader.close();
		return result;
	}
	
	/**
	 * Return the data as CSV, which will be a list of arrays of strings. Features options for parsing the CSV.
	 * @param separator
	 * @param quotechar
	 * @param escape
	 * @return
	 * @throws IOException
	 */
	public List<String[]> asCSV(char separator, char quotechar, char escape) throws IOException {
		CSVReader reader = new CSVReader(new StringReader(text), separator, quotechar, escape);
		List<String[]> result = reader.readAll();
		reader.close();
		return result;
	}
	
	
	/**
	 * Returns the text as an XML document, which can then be queried using 
	 * the native <a href='http://docs.oracle.com/javase/tutorial/jaxp/xslt/xpath.html'>XPath</a>. 
	 * @return
	 * @throws Exception
	 */
	public Document asXML() throws Exception {
		return XMLConverter.convert(text);
	}
	
	/**
	 * Returns the text as an HTML document, which can then be queried using the method evaluateXPath, or any of
	 * the methods related to the <a href='http://htmlcleaner.sourceforge.net/doc/index.html'>htmlcleaner</a> library. 
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public TagNode asHTML() throws SAXException, IOException {
		return HTMLConverter.convert(text); 
	}

}
