package realtimeweb.stickyweb;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import realtimeweb.stickyweb.exceptions.StickyWebNotInCacheException;


/**
 * Class for building up a Web Request; uses the fluent pattern.
 * @author acbart
 *
 */
public class StickyWebRequest {
	
	private boolean online;
	private LocalCache cache;
	private Protocol protocol;
	private String url;
	private Map<String, String> arguments;
	private Pattern pattern;
	private ArrayList<String> indices;
	
	public StickyWebRequest setOnline(boolean online) {
		this.online = online;
		return this;
	}
	
	public StickyWebRequest setPattern(Pattern pattern) {
		this.pattern = pattern;
		return this;
	}
	
	public StickyWebRequest setIndexes(ArrayList<String> indexes) {
		this.indices = indexes;
		return this;
	}

	public StickyWebRequest(String url, Map<String, String> arguments,
			LocalCache cache, Protocol protocol) {
		this.url = url;
		this.arguments = arguments;
		this.cache = cache;
		this.protocol = protocol;
		this.indices = null;
	}
	
	public synchronized StickyWebResponse execute() throws IllegalStateException, IOException, URISyntaxException, StickyWebNotInCacheException {
		if (online) {
			switch (protocol) {
			default: case GET: return new StickyWebResponse(WebConnection.get(url, arguments));
			case POST: return new StickyWebResponse(WebConnection.post(url, arguments));
			case PUT: return new StickyWebResponse(WebConnection.put(url, arguments));
			case DELETE: return new StickyWebResponse(WebConnection.delete(url, arguments));
			}
		} else {
			return new StickyWebResponse(cache.get(url, arguments, this.indices, this.pattern));
		}
	}

}
