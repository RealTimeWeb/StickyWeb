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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import realtimeweb.stickyweb.exceptions.StickyWebInternetException;

class WebConnection {
	
	private static DefaultHttpClient httpclient = new DefaultHttpClient();
	
	private static String execute(HttpUriRequest service) throws StickyWebInternetException {
		// Execute get request
		try {
			HttpResponse response = httpclient.execute((HttpUriRequest) service);
			return EntityUtils.toString(response.getEntity());
		} catch (IOException ioe) {
			throw new StickyWebInternetException(ioe.getMessage());
		}
	}
	
	private static Map<String, String> cleanArguments(Map<String, String> arguments) {
		if (arguments == null) {
			return new HashMap<String, String>();
		} else {
			return arguments;
		}
	}

	/**
	 * In the interests of simplicity, we assume only Query parameters are used, rather than Form (like with POST).
	 * @param url
	 * @param arguments
	 * @param accessToken 
	 * @param service2 
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws StickyWebInternetException 
	 */
	static String get(String url, Map<String, String> arguments, OAuthService oauthService, Token accessToken) throws IllegalStateException, IOException, URISyntaxException, StickyWebInternetException {
		arguments = cleanArguments(arguments);
		if (oauthService == null) {
			HttpGet service= new HttpGet(url);
			((HttpRequestBase) service).setURI(buildQueryString(service.getURI(), arguments));
			return execute(service);
		} else {
			OAuthRequest request = new OAuthRequest(Verb.GET, url);
			for (Entry<String, String> argument: arguments.entrySet()) {
	            String key = argument.getKey();
	            String value = argument.getValue();
	            request.addQuerystringParameter(key, value);
	        }
	        oauthService.signRequest(accessToken, request);
	        return request.send().getBody();
		}
	}
	
	private static URI buildQueryString(URI uri, Map<String, String> arguments) throws URISyntaxException {
		URIBuilder ub = new URIBuilder(uri);
		for (Entry<String, String> qa : arguments.entrySet()) {
			ub.addParameter(qa.getKey(), qa.getValue());
		}
		return ub.build();
	}
	
	private static HttpEntity buildEntity(Map<String, String> arguments) throws UnsupportedEncodingException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (Entry<String, String> ba : arguments.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(ba.getKey(), ba.getValue()));
		}
		return new UrlEncodedFormEntity(nameValuePairs);
	}

	/**
	 * In the interests of simplicity, we assume only Form parameters are used, rather than Query (like with GET).
	 * @param url
	 * @param arguments
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws StickyWebInternetException 
	 */
	static String post(String url, Map<String, String> arguments, OAuthService oauthService, Token accessToken) throws IllegalStateException, IOException, URISyntaxException, StickyWebInternetException {
		arguments = cleanArguments(arguments);		
		HttpPost service= new HttpPost(url);
		service.setEntity(buildEntity(arguments));
		return execute(service);
	}
	
	/**
	 * In the interests of simplicity, we assume only Query parameters are used, rather than Form (like with POST).
	 * @param url
	 * @param arguments
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws StickyWebInternetException 
	 */
	static String delete(String url, Map<String, String> arguments, OAuthService oauthService, Token accessToken) throws URISyntaxException, ClientProtocolException, IOException, StickyWebInternetException {
		arguments = cleanArguments(arguments);
		HttpDelete service= new HttpDelete(url);
		((HttpRequestBase) service).setURI(buildQueryString(service.getURI(), arguments));
		return execute(service);
	}
	
	/**
	 * In the interests of simplicity, we assume only Query parameters are used, rather than Form (like with POST).
	 * @param url
	 * @param arguments
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws StickyWebInternetException 
	 */
	static String put(String url, Map<String, String> arguments, OAuthService oauthService, Token accessToken) throws URISyntaxException, ClientProtocolException, IOException, StickyWebInternetException {
		arguments = cleanArguments(arguments);
		HttpPut service= new HttpPut(url);
		((HttpRequestBase) service).setURI(buildQueryString(service.getURI(), arguments));
		return execute(service);
	}

}
