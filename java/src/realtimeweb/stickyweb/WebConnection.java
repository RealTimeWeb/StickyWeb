package realtimeweb.stickyweb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.boye.httpclientandroidlib.*;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.client.methods.HttpDelete;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.client.methods.HttpPut;
import ch.boye.httpclientandroidlib.client.methods.HttpRequestBase;
import ch.boye.httpclientandroidlib.client.methods.HttpUriRequest;
import ch.boye.httpclientandroidlib.client.utils.URIBuilder;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import ch.boye.httpclientandroidlib.util.EntityUtils;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import realtimeweb.stickyweb.exceptions.StickyWebInternetException;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidPostArguments;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidQueryString;

class WebConnection {

	private static DefaultHttpClient httpclient = new DefaultHttpClient();

	private static String execute(HttpUriRequest service)
			throws StickyWebInternetException {
		// Execute get request
		try {
			HttpResponse response = httpclient
					.execute((HttpUriRequest) service);
			if (response.getEntity() != null) {
				return EntityUtils.toString(response.getEntity());
			} else {
				return "";
			}
		} catch (IOException ioe) {
			throw new StickyWebInternetException(ioe.getMessage());
		}
	}

	private static Map<String, String> cleanArguments(
			Map<String, String> arguments) {
		if (arguments == null) {
			return new HashMap<String, String>();
		} else {
			return arguments;
		}
	}

	/**
	 * In the interests of simplicity, we assume only Query parameters are used,
	 * rather than Form (like with POST).
	 * 
	 * @param url
	 * @param arguments
	 * @param accessToken
	 * @param service2
	 * @return
	 * @throws StickyWebInternetException
	 * @throws StickyWebInvalidQueryString
	 */
	static String get(String url, Map<String, String> arguments,
			OAuthService oauthService, Token accessToken)
			throws StickyWebInternetException, StickyWebInvalidQueryString {
		arguments = cleanArguments(arguments);
		if (oauthService == null) {
			HttpGet service = new HttpGet(url);
			((HttpRequestBase) service).setURI(buildQueryString(
					service.getURI(), arguments));
			return execute(service);
		} else {
			OAuthRequest request = new OAuthRequest(Verb.GET, url);
			for (Entry<String, String> argument : arguments.entrySet()) {
				String key = argument.getKey();
				String value = argument.getValue();
				request.addQuerystringParameter(key, value);
			}
			oauthService.signRequest(accessToken, request);
			return request.send().getBody();
		}
	}

	/**
	 * Build up the query string for a URL.
	 * 
	 * @param uri
	 * @param arguments
	 * @return
	 * @throws StickyWebInvalidQueryString
	 */
	private static URI buildQueryString(URI uri, Map<String, String> arguments)
			throws StickyWebInvalidQueryString {
		URIBuilder ub = new URIBuilder(uri);
		for (Entry<String, String> qa : arguments.entrySet()) {
			ub.addParameter(qa.getKey(), qa.getValue());
		}
		try {
			return ub.build();
		} catch (URISyntaxException e) {
			throw new StickyWebInvalidQueryString(e.getMessage());
		}
	}

	/**
	 * Build an HttpEntity, used for making non-GET requests.
	 * 
	 * @param arguments
	 * @return
	 * @throws StickyWebInvalidPostArguments
	 */
	private static HttpEntity buildEntity(Map<String, String> arguments)
			throws StickyWebInvalidPostArguments {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (Entry<String, String> ba : arguments.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(ba.getKey(), ba
					.getValue()));
		}
		try {
			return new UrlEncodedFormEntity(nameValuePairs);
		} catch (UnsupportedEncodingException e) {
			throw new StickyWebInvalidPostArguments(e.getMessage());
		}
	}

	/**
	 * In the interests of simplicity, we assume only Form parameters are used,
	 * rather than Query (like with GET).
	 * 
	 * @param url
	 * @param arguments
	 * @return
	 * @throws StickyWebInternetException
	 * @throws StickyWebInvalidPostArguments
	 */
	static String post(String url, Map<String, String> arguments,
			OAuthService oauthService, Token accessToken)
			throws StickyWebInternetException, StickyWebInvalidPostArguments {
		arguments = cleanArguments(arguments);
		HttpPost service = new HttpPost(url);
		service.setEntity(buildEntity(arguments));
		return execute(service);
	}

	/**
	 * In the interests of simplicity, we assume only Query parameters are used,
	 * rather than Form (like with POST).
	 * 
	 * @param url
	 * @param arguments
	 * @return
	 * @throws StickyWebInternetException
	 * @throws StickyWebInvalidQueryString
	 */
	static String delete(String url, Map<String, String> arguments,
			OAuthService oauthService, Token accessToken)
			throws StickyWebInternetException, StickyWebInvalidQueryString {
		arguments = cleanArguments(arguments);
		HttpDelete service = new HttpDelete(url);
		((HttpRequestBase) service).setURI(buildQueryString(service.getURI(),
				arguments));
		return execute(service);
	}

	/**
	 * In the interests of simplicity, we assume only Query parameters are used,
	 * rather than Form (like with POST).
	 * 
	 * @param url
	 * @param arguments
	 * @return
	 * @throws StickyWebInternetException
	 * @throws StickyWebInvalidQueryString
	 */
	static String put(String url, Map<String, String> arguments,
			OAuthService oauthService, Token accessToken)
			throws StickyWebInternetException, StickyWebInvalidQueryString {
		arguments = cleanArguments(arguments);
		HttpPut service = new HttpPut(url);
		((HttpRequestBase) service).setURI(buildQueryString(service.getURI(),
				arguments));
		return execute(service);
	}

}
