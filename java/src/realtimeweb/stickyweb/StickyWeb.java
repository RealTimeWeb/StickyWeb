package realtimeweb.stickyweb;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

import org.json.simple.parser.ParseException;

public class StickyWeb {
	
	private LocalCache cache;
	
	public StickyWeb() {
		cache = new LocalCache();
	}
	
	public StickyWeb(InputStream dataSource) throws IOException, ParseException {
		cache = new LocalCache(dataSource);
	}
	
	public StickyWebRequest get(String url, Map<String, String> arguments) throws IllegalStateException, IOException, URISyntaxException {
		return new StickyWebRequest(url, arguments, cache, Protocol.GET);
	}
	
	public StickyWebRequest post(String url, Map<String, String> arguments) throws IllegalStateException, IOException, URISyntaxException {
		return new StickyWebRequest(url, arguments, cache, Protocol.POST);
	}
	
	public StickyWebRequest delete(String url, Map<String, String> arguments) throws IllegalStateException, IOException, URISyntaxException {
		return new StickyWebRequest(url, arguments, cache, Protocol.DELETE);
	}
	
	public StickyWebRequest PUT(String url, Map<String, String> arguments) throws IllegalStateException, IOException, URISyntaxException {
		return new StickyWebRequest(url, arguments, cache, Protocol.PUT);
	}

}
