package realtimeweb.stickyweb;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.parser.ParseException;

import realtimeweb.stickyweb.converters.JsonConverter;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceParseException;
import realtimeweb.stickyweb.exceptions.StickyWebLoadDataSourceException;
import realtimeweb.stickyweb.exceptions.StickyWebNotInCacheException;

class LocalCache {

	protected HashMap<String, Output> data;
	protected HashMap<String, Object> metadata;

	/**
	 * Creates a new, empty LocalCache
	 */
	LocalCache() {
		data = new HashMap<String, Output>();
		metadata = new HashMap<String, Object>();
	}

	/**
	 * Creates a new LocalCache from the InputStream
	 * 
	 * @param dataSource
	 * @throws StickyWebDataSourceNotFoundException
	 * @throws StickyWebLoadDataSourceException
	 * @throws StickyWebParseException
	 * @throws StickyWebDataSourceMalformedException
	 */
	LocalCache(InputStream dataSource)
			throws StickyWebDataSourceNotFoundException,
			StickyWebDataSourceParseException, StickyWebLoadDataSourceException {
		setDataSource(dataSource);
	}

	/**
	 * Replaces the data in the cache with the data in the InputStream.
	 * 
	 * @param dataSource
	 * 
	 * @throws StickyWebDataSourceNotFoundException
	 * @throws StickyWebLoadDataSourceException
	 * @throws StickyWebParseException
	 * @throws StickyWebDataSourceMalformedException
	 */
	@SuppressWarnings("unchecked")
	public void setDataSource(InputStream dataSource)
			throws StickyWebDataSourceNotFoundException,
			StickyWebDataSourceParseException, StickyWebLoadDataSourceException {
		// Load the new data source into memory
		if (dataSource == null) {
			throw new StickyWebDataSourceNotFoundException(
					"The given InputStream was null; check to make sure that the file exists.");
		}
		Map<String, Object> jsonData = JsonConverter.convertToMap(dataSource);
		if (!jsonData.containsKey("data")) {
			throw new StickyWebDataSourceParseException(
					"Unable to find \"data\" key in given Data Source.");
		}
		replaceData((Map<String, Object>) jsonData.get("data"));
		if (jsonData.containsKey("metadata")) {
			this.metadata = (HashMap<String, Object>) jsonData.get("metadata");
		}
	}

	/**
	 * Completely resets the clock for each new output.
	 */
	void reset() {
		for (Output output : data.values()) {
			output.reset();
		}
	}

	/**
	 * Replaces the data in the cache with the data in the Map.
	 * 
	 * @param newData
	 * @throws ParseException
	 * @throws StickyWebDataSourceParseException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void replaceData(Map<String, Object> newData)
			throws StickyWebDataSourceParseException {
		data = new HashMap<String, Output>();
		for (Entry<String, Object> es : newData.entrySet()) {
			if (es.getValue() instanceof ArrayList) {
				for (Object o : (ArrayList) es.getValue()) {
					if (!(o instanceof String)) {
						throw new StickyWebDataSourceParseException(
								"Input "
										+ es.getValue()
										+ " does not have a list of strings. One of the elements is "
										+ o.getClass() + " (" + o + ").");
					}
				}
				put(es.getKey(), (ArrayList<String>) es.getValue());
			} else {
				throw new StickyWebDataSourceParseException("Input "
						+ es.getValue() + " does not have a list of strings.");
			}
		}
	}

	/**
	 * Given a URL and its arguments, convert it to a hashable request FIXME: I
	 * strongly suspect that this is insufficient. If anyone knows of a better
	 * library to uniformly hash requests, please let me know.
	 * 
	 * @param url
	 * @param query_arguments
	 * @param indices
	 * @return
	 */
	public static String hashRequest(String url,
			Map<String, String> query_arguments, ArrayList<String> indices) {
		StringBuilder completeQuery = new StringBuilder(url);
		completeQuery.append("?");
		if (!indices.isEmpty() || (indices == null && query_arguments != null)) {
			ArrayList<String> actualIndices;
			if (indices == null) {
				actualIndices = new ArrayList<String>(query_arguments.keySet());
			} else {
				actualIndices = indices;
			}
			for (String key : actualIndices) {
				completeQuery
						.append(key + "=" + query_arguments.get(key) + "&");
			}
			// Remove any trailing &
			if (!indices.isEmpty()) {
				completeQuery.deleteCharAt(completeQuery.length() - 1);
			}
		}
		return completeQuery.toString();
	}

	/**
	 * Retrieves an element from the LocalCache.
	 * 
	 * @param url
	 * @param arguments
	 * @param indices
	 * @return
	 * @throws StickyWebNotInCacheException
	 *             if the element is not in the cache.
	 */
	String get(String url, Map<String, String> arguments,
			ArrayList<String> indices) throws StickyWebNotInCacheException {
		String request = hashRequest(url, arguments, indices);
		if (data.containsKey(request)) {
			return data.get(request).get();
		} else {
			throw new StickyWebNotInCacheException(request);
		}
	}

	/**
	 * Returns whether there are more elements for this request.
	 * 
	 * @param url
	 * @param arguments
	 * @param indices
	 * @param pattern
	 * @return
	 * @throws StickyWebNotInCacheException
	 */
	boolean hasMore(String url, Map<String, String> arguments,
			ArrayList<String> indices) throws StickyWebNotInCacheException {
		String request = hashRequest(url, arguments, indices);
		if (data.containsKey(request)) {
			return data.get(request).hasMore();
		} else {
			throw new StickyWebNotInCacheException(request);
		}
	}

	/**
	 * Store this new list of outputs into the cache.
	 * 
	 * @param key
	 * @param value
	 */
	private void put(String key, ArrayList<String> value) {
		data.put(key, new Output(value));
	}
}
