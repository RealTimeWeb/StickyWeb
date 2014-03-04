package realtimeweb.stickyweb;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONValue;

import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceParseException;
import realtimeweb.stickyweb.exceptions.StickyWebLoadDataSourceException;

public class EditableCache extends LocalCache {

	/**
	 * Sets the pattern for this particular key.
	 * 
	 * @param key
	 * @param pattern
	 */
	public void setPattern(String key, Pattern pattern) {
		if (this.data.containsKey(key)) {
			Output element = this.data.get(key);
			element.setPattern(pattern);
		} else {
			this.data.put(key, new Output(pattern));
		}
	}

	/**
	 * Set the data for this key. The *data* should not start with the pattern.
	 * 
	 * @param key
	 * @param pattern
	 * @param data
	 *            The list of strings that will be returned for this key. Should
	 *            not start with the pattern.
	 */
	public void setData(String key, Pattern pattern, ArrayList<String> data) {
		if (this.data.containsKey(key)) {
			Output element = this.data.get(key);
			element.setPattern(pattern);
		} else {
			this.data.put(key, new Output(pattern, data));
		}
	}

	/**
	 * Removes the data associated with the key and index
	 * 
	 * @param key
	 * @param index
	 */
	public void removeData(String key, int index) {
		Output element = this.data.get(key);
		element.removeData(index);
	}

	/**
	 * Removes all the data associated with the key
	 * 
	 * @param key
	 */
	public void removeAllData(String key) {
		Output element = this.data.get(key);
		element.removeAllData();
	}

	/**
	 * Adds new data for the key. If the key's pattern does not exist, the
	 * REPEAT pattern is assumed.
	 * 
	 * @param key
	 * @param data
	 */
	public void addData(String key, String data) {
		if (this.data.containsKey(key)) {
			Output element = this.data.get(key);
			element.add(data);
		} else {
			this.data.put(key, new Output(data));
		}
	}

	/**
	 * Returns the data associated with a particular key and index.
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public String getData(String key, int index) {
		Output element = this.data.get(key);
		return element.get(index);
	}

	/**
	 * Returns all the data associated with the key. Note that this returns the
	 * internal list used for convenience. If you edit the list, you will affect
	 * the contents of the cache!
	 * 
	 * @param key
	 * @return
	 */
	public List<String> getAllData(String key) {
		Output element = this.data.get(key);
		return element.getAll();
	}

	/**
	 * Correctly saves the cache to the stream, overwriting it if present.
	 * 
	 * @param stream
	 * @return
	 * @throws StickyWebDataSourceNotFoundException
	 * @throws StickyWebDataSourceParseException
	 * @throws StickyWebLoadDataSourceException
	 */
	public void saveToStream(OutputStream stream)
			throws StickyWebDataSourceNotFoundException,
			StickyWebDataSourceParseException, StickyWebLoadDataSourceException {
		if (stream == null) {
			throw new StickyWebDataSourceNotFoundException(
					"The given OutputStream was null; check to make sure that the stream exists.");
		}
		HashMap<String, Object> dataSource = new HashMap<String, Object>();
		dataSource.put("metadata", this.metadata);
		HashMap<String, Object> actualData = new HashMap<String, Object>();
		for (Entry<String, Output> entry : this.data.entrySet()) {
			actualData.put(entry.getKey(), entry.getValue().recombine());
		}
		dataSource.put("data", actualData);
		PrintWriter streamPrinter = new PrintWriter(stream);
		streamPrinter.print(JSONValue.toJSONString(dataSource));
		streamPrinter.close();
	}
	
	public List<String> getKeys() {
		return new ArrayList<String>(this.data.keySet());
	}
}
