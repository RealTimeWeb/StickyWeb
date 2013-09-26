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

import au.com.bytecode.opencsv.CSVReader;

public class StickyWebResponse {
	
	private String text;

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

	public String asText() {
		return text;
	}
	
	public Map<String, Object> asJSON() throws ParseException {
		return JsonConverter.convertToMap(text);
	}
	
	public List<Object> asJSONArray() throws ParseException {
		return JsonConverter.convertToList(text);
	}
	
	public List<String[]> asCSV() throws IOException {
		CSVReader reader = new CSVReader(new StringReader(text));
		List<String[]> result = reader.readAll();
		reader.close();
		return result;
	}
	
	public List<String[]> asCSV(char separator, char quotechar, char escape) throws IOException {
		CSVReader reader = new CSVReader(new StringReader(text), separator, quotechar, escape);
		List<String[]> result = reader.readAll();
		reader.close();
		return result;
	}
	
	
	/**
	 * Returns the text as an XML document, which can then be queried using XPath. 
	 * @return
	 * @throws Exception
	 */
	public Document asXML() throws Exception {
		return XMLConverter.convert(text);
	}
	
	public TagNode asHTML() throws SAXException, IOException {
		return HTMLConverter.convert(text); 
	}

}
