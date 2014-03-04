package realtimeweb.stickyweb;

import java.io.InputStream;
import java.util.Map;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceParseException;
import realtimeweb.stickyweb.exceptions.StickyWebLoadDataSourceException;

public class StickyWeb {

	private LocalCache cache;
	private Token accessToken;
	private OAuthService service;

	public StickyWeb() {
		cache = new LocalCache();
		accessToken = null;
		service = null;
	}

	public StickyWeb(InputStream dataSource)
			throws StickyWebDataSourceNotFoundException,
			StickyWebDataSourceParseException, StickyWebLoadDataSourceException {
		if (dataSource != null) {
			cache = new LocalCache(dataSource);
		} else {
			cache = null;
		}
		accessToken = null;
		service = null;
	}

	public StickyWeb setAuthentication(String consumerKey,
			String consumerSecret, String token, String tokenSecret) {
		this.service = new ServiceBuilder()
				.provider(ServiceAuthentication.class).apiKey(consumerKey)
				.apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
		return this;
	}

	public StickyWebRequest get(String url, Map<String, String> arguments) {
		return new StickyWebRequest(url, arguments, cache, Protocol.GET,
				service, accessToken);
	}

	public StickyWebRequest post(String url, Map<String, String> arguments) {
		return new StickyWebRequest(url, arguments, cache, Protocol.POST,
				service, accessToken);
	}

	public StickyWebRequest delete(String url, Map<String, String> arguments){
		return new StickyWebRequest(url, arguments, cache, Protocol.DELETE,
				service, accessToken);
	}

	public StickyWebRequest put(String url, Map<String, String> arguments) {
		return new StickyWebRequest(url, arguments, cache, Protocol.PUT,
				service, accessToken);
	}

}
