package realtimeweb.stickyweb;

import java.util.ArrayList;
import java.util.List;

class Output {
	/**
	 * Create a new Output object from a list of outputs.
	 * @param value
	 */
	Output(ArrayList<String> value) {
		clock = 0;
		pattern = Pattern.valueOf(value.get(0).toUpperCase());
		if (value.size() == 1) {
			data = new ArrayList<String>();
		} else {
			data = value.subList(1, value.size()-1);
		}
	}
	private List<String> data;
	private int clock;
	private Pattern pattern;
	
	/**
	 * Change the pattern for the data.
	 * @param pattern
	 */
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	
	/**
	 * Creates a new Output without any data.
	 * @param pattern
	 */
	Output(Pattern pattern) {
		this.clock = 0;
		this.pattern = pattern;
		this.data = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param pattern
	 * @param data The string data (without a pattern as the first element)
	 */
	public Output(Pattern pattern, ArrayList<String> data) {
		this.clock = 0;
		this.pattern = pattern;
		this.data = data;
	}

	public Output(String data) {
		this.clock = 0;
		this.pattern = Pattern.REPEAT;
		this.data = new ArrayList<String>();
		this.data.add(data);
	}

	/**
	 * Returns the current data, based on the clock. If there is no more data,
	 * then the result is returned based on the pattern:<ul>
	 * 	<li>RESTART: turn the clock back to 0 and return first result.</li>
	 *  <li>REPEAT: turn the clock back to the final element and return it again.</li>
	 *  <li>EMPTY: return null, which should be turned into a relevant "empty" element.</li>
	 *  </ul>
	 * @param pattern
	 * @return
	 */
	String get() {
		if (data.size() == 0) {
			return null;
		}
		if (!hasMore()) {
			switch (pattern) {
			case EMPTY:
				return null;
			case RESTART:
				clock = 0;
				break;
			default: case REPEAT:
				clock = data.size()-1;
				break;
			}
		}
		return data.get(clock++);
	}
	
	/**
	 * Returns whether or not there are more items for this output.
	 * @return
	 */
	boolean hasMore() {
		return clock < data.size();
	}
	
	/**
	 * Resets the clock on this input, so that it returns results from the beginning.
	 */
	void reset() {
		clock = 0;
	}

	public void removeData(int index) {
		this.data.remove(index);
	}

	public void removeAllData() {
		this.data.clear();
	}

	public String get(int index) {
		return this.data.get(index);
	}

	public List<String> getAll() {
		return this.data;
	}

	public ArrayList<String> recombine() {
		ArrayList<String> output = new ArrayList<String>(this.data);
		output.add(0, this.pattern.toString().toLowerCase());
		return output;
	}

	public void add(String data) {
		this.data.add(data);
	}
}
