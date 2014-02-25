package realtimeweb.stickyweb.converters;

import java.io.IOException;
import java.io.StringWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import realtimeweb.stickyweb.exceptions.StickyWebDataSourceParseException;
import realtimeweb.stickyweb.exceptions.StickyWebJsonResponseParseException;
import realtimeweb.stickyweb.exceptions.StickyWebLoadDataSourceException;

/**
 * An internal class that provides a convenient converter for JSON formatted
 * data. This class will convert JSON maps to HashMaps and JSON lists to
 * ArrayLists.
 * 
 * @author acbart
 * 
 */
public class JsonConverter {
	
	private static JSONParser jsonParser = new JSONParser();
	
	private static ContainerFactory CONTAINER_FACTORY = new ContainerFactory() {
		@SuppressWarnings("rawtypes")
		public List creatArrayContainer() {
			return new ArrayList();
		}

		@SuppressWarnings("rawtypes")
		public Map createObjectContainer() {
			return new HashMap();
		}

	};

	/**
	 * Given a string of JSON data, convert it to a Map.
	 * @param input
	 * @return
	 * @throws StickyWebJsonResponseParseException 
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertToMap(String input) throws StickyWebJsonResponseParseException {
		try {
			return (Map<String, Object>) jsonParser.parse(input,
					CONTAINER_FACTORY);
		} catch (ParseException e) {
			throw new StickyWebJsonResponseParseException(e.getMessage());
		}
	}

	/**
	 * Given a string of JSON data, convert it to a List.
	 * @param input
	 * @return
	 * @throws StickyWebJsonResponseParseException 
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> convertToList(String input) throws StickyWebJsonResponseParseException {
		try {
			return (List<Object>) jsonParser.parse(input,
					CONTAINER_FACTORY);
		} catch (ParseException e) {
			throw new StickyWebJsonResponseParseException(e.getMessage());
		}
	}
	
	public static Map<String, Object> convertToMap(InputStream file) throws StickyWebDataSourceParseException, StickyWebLoadDataSourceException {
		try {
			StringWriter writer = new StringWriter();
			IOUtils.copy(file, writer);
			return convertToMap(writer.toString());
		} catch (StickyWebJsonResponseParseException pe) {
			throw new StickyWebDataSourceParseException("Couldn't parse InputStream " + file.toString() + ". " + pe.getMessage());
		} catch (IOException ioe) {
			throw new StickyWebLoadDataSourceException("Couldn't load InputStream "+ file.toString() + ". " + ioe.getMessage());
		}
	}
}
