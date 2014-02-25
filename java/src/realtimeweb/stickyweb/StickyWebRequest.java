package realtimeweb.stickyweb;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebInternetException;
import realtimeweb.stickyweb.exceptions.StickyWebNotInCacheException;

/**
 * Class for building up a Web Request; uses the fluent pattern.
 * 
 * @author acbart
 * 
 */
public class StickyWebRequest {

	private boolean online;
	private LocalCache cache;
	private Protocol protocol;
	private String url;
	private Map<String, String> arguments;
	private ArrayList<String> indices;
	private OAuthService service;
	private Token accessToken;

	/**
	 * Set whether results should be returned from the web or from the local
	 * cache.
	 * 
	 * @param online
	 * @return
	 * @throws StickyWebDataSourceNotFoundException
	 */
	public StickyWebRequest setOnline(boolean online)
			throws StickyWebDataSourceNotFoundException {
		if (!online && cache == null) {
			throw new StickyWebDataSourceNotFoundException(
					"The given InputStream was null; check to make sure that the file exists.");
		} else {
			this.online = online;
		}
		return this;
	}

	/**
	 * Specifies what parameters should be used when hashing a request; that is,
	 * what indexes uniquely identify a call. If null, then all parameters will
	 * be used.
	 * 
	 * @param indexes
	 * @return
	 */
	public StickyWebRequest setIndexes(ArrayList<String> indexes) {
		this.indices = indexes;
		return this;
	}

	/**
	 * Creates a new StickyWebRequest with parameters.
	 * 
	 * @param url
	 *            The url of the resource.
	 * @param arguments
	 *            Un-encoded data that will be sent along with the request.
	 * @param cache
	 *            A reference to the local system's cache
	 * @param protocol
	 *            The method that the resource will be accessed.
	 * @param accessToken
	 * @param service
	 */
	public StickyWebRequest(String url, Map<String, String> arguments,
			LocalCache cache, Protocol protocol, OAuthService service,
			Token accessToken) {
		this.url = url;
		this.arguments = arguments;
		this.cache = cache;
		this.protocol = protocol;
		this.indices = null;
		this.service = service;
		this.accessToken = accessToken;
	}

	/**
	 * Gets the response for this request, from either the online service or the
	 * local cache.
	 * 
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws StickyWebNotInCacheException
	 * @throws StickyWebInternetException
	 */
	public synchronized StickyWebResponse execute()
			throws IllegalStateException, IOException, URISyntaxException,
			StickyWebNotInCacheException, StickyWebInternetException {
		if (online) {
			switch (protocol) {
			default:
			case GET:
				return new StickyWebResponse(WebConnection.get(url, arguments,
						service, accessToken));
			case POST:
				return new StickyWebResponse(WebConnection.post(url, arguments,
						service, accessToken));
			case PUT:
				return new StickyWebResponse(WebConnection.put(url, arguments,
						service, accessToken));
			case DELETE:
				return new StickyWebResponse(WebConnection.delete(url,
						arguments, service, accessToken));
			}
		} else {
			return new StickyWebResponse(
					cache.get(url, arguments, this.indices));
		}
	}

	/**
	 * Returns the internal hash of the url and its arguments that will be used
	 * to look up the data in the cache.
	 * 
	 * @return
	 */
	public String getHashedRequest() {
		return LocalCache.hashRequest(url, arguments, indices);
	}

}
