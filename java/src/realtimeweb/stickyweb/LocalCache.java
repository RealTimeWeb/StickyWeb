package realtimeweb.stickyweb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.parser.ParseException;

import realtimeweb.stickyweb.converters.JsonConverter;
import realtimeweb.stickyweb.exceptions.StickyWebNotInCacheException;

public class LocalCache {
	
	private HashMap<String, Output> data;
	
	/**
	 * Creates a new, empty LocalCache
	 */
	public LocalCache() {
		data = new HashMap<String, Output>();
	}

	/**
	 * Creates a new LocalCache from the InputStream
	 * @param dataSource
	 * @throws IOException
	 * @throws ParseException
	 */
	public LocalCache(InputStream dataSource) throws IOException, ParseException {
		setDataSource(dataSource);
	}
	
	/**
	 * Replaces the data in the cache with the data in the InputStream.
	 * @param dataSource
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public void setDataSource(InputStream dataSource) throws IOException, ParseException {
		// Load the new data source into memory
		if (dataSource == null) {
			throw new IOException("The given InputStream was null; check to make sure that the file exists.");
		}
		// TODO Wrap in StickyWebException
		Map<String, Object> jsonData = JsonConverter.convertToMap(dataSource);
		replaceData((Map<String, Object>) jsonData.get("data"));
	}
	
	/**
	 * Completely resets the clock for each new output.
	 */
	public void reset() {
		for (Output output : data.values()) {
			output.reset();
		}
	}
	
	/**
	 * Replaces the data in the cache with the data in the Map.
	 * @param newData
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private void replaceData(Map<String, Object> newData) throws ParseException {
		data = new HashMap<String, Output>();
		for (Entry<String, Object> es : newData.entrySet()) {
			if (es.getValue() instanceof ArrayList) {
				put(es.getKey(), (ArrayList<String>)es.getValue());
			} else {
				// TODO: Wrap in StickyWebException
				throw new ParseException(0);
			}
		}
	}

	/**
	 * Given a URL and its arguments, convert it to a hashable request
	 * FIXME: I strongly suspect that this is insufficient. If anyone knows of a better 
	 * library to uniformly hash requests, please let me know.
	 * @param url
	 * @param query_arguments
	 * @param indices 
	 * @return
	 */
	private static String hashRequest(String url, Map<String, String> query_arguments, ArrayList<String> indices) {
		StringBuilder completeQuery = new StringBuilder(url);
		if (!indices.isEmpty() || (indices == null && query_arguments != null)) {
			ArrayList<String> actualIndices;
			if (indices == null) {
				actualIndices = new ArrayList<String>(query_arguments.keySet());
			} else {
				actualIndices = indices;
			}
			completeQuery.append("?");
			for (String key : actualIndices) {
				completeQuery.append(key + "=" + query_arguments.get(key) + "&");
			}
			// If empty list, remove ?
			// Otherwise, remove trailing &
			completeQuery.deleteCharAt(completeQuery.length()-1);
		}
		return completeQuery.toString();
	}

	/**
	 * Retrieves an element from the LocalCache.
	 * @param url
	 * @param arguments
	 * @param indices 
	 * @param pattern
	 * @return
	 * @throws StickyWebNotInCacheException if the element is not in the cache.
	 */
	public String get(String url, Map<String, String> arguments, ArrayList<String> indices, Pattern pattern) throws StickyWebNotInCacheException {
		String request = hashRequest(url, arguments, indices);
		if (data.containsKey(request)) {
			return data.get(request).get(pattern);
		} else {
			throw new StickyWebNotInCacheException(request);
		}
	}
	
	/**
	 * Returns whether there are more elements for this request.
	 * @param url
	 * @param arguments
	 * @param indices
	 * @param pattern
	 * @return
	 * @throws StickyWebNotInCacheException 
	 */
	public boolean hasMore(String url, Map<String, String> arguments, ArrayList<String> indices, Pattern pattern) throws StickyWebNotInCacheException {
		String request = hashRequest(url, arguments, indices);
		if (data.containsKey(request)) {
			return data.get(request).hasMore();
		} else {
			throw new StickyWebNotInCacheException(request);
		}
	}
	
	/**
	 * Store this new list of outputs into the cache.
	 * @param key
	 * @param value
	 */
	private void put(String key, ArrayList<String> value) {
		data.put(key, new Output(value));
	}
}
