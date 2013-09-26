package realtimeweb.stickyweb;

import java.util.ArrayList;

public class Output {
	public Output(ArrayList<String> value) {
		clock = 0;
		data = value;
	}
	private ArrayList<String> data;
	private int clock;
	
	/**
	 * Returns the current data, based on the clock. If there is no more data,
	 * then the result is returned based on the pattern:<ul>
	 * 	<li>RESTART: turn the clock back to 0 and return first result.</li>
	 *  <li>REPEAT: turn the clock back to the final element and return it again.</li>
	 *  <li>EMPTY: return null, which should be turned into a relevant "empty" element.</li>
	 *  </u>
	 * @param pattern
	 * @return
	 */
	public String get(Pattern pattern) {
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
	public boolean hasMore() {
		return clock < data.size();
	}
	
	/**
	 * Resets the clock on this input, so that it returns results from the beginning.
	 */
	public void reset() {
		clock = 0;
	}
}
