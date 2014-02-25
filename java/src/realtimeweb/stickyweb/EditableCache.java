package realtimeweb.stickyweb;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONValue;

import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceParseException;
import realtimeweb.stickyweb.exceptions.StickyWebLoadDataSourceException;

public class EditableCache extends LocalCache {

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

	public void removeData(String key, int index) {
		Output element = this.data.get(key);
		element.removeData(index);
	}

	public void removeAllData(String key) {
		Output element = this.data.get(key);
		element.removeAllData();
	}

	public void addData(String key, String data) {
		Output element = this.data.get(key);
		element.removeAllData();
	}

	public String getData(String key, int index) {
		Output element = this.data.get(key);
		return element.get(index);
	}

	public List<String> getAllData(String key) {
		Output element = this.data.get(key);
		return element.getAll();
	}
	
	public String saveToStream(OutputStream stream)
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
		return JSONValue.toJSONString(dataSource);
	}
}
