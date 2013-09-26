package realtimeweb.stickyweb;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class WebConnection {

	/**
	 * In the interests of simplicity, we assume only Query parameters are used, rather than Form (like with POST).
	 * @param url
	 * @param arguments
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String get(String url, Map<String, String> arguments) throws IllegalStateException, IOException, URISyntaxException {
		if (arguments == null) {
			arguments = new HashMap<String, String>();
		}
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet service= new HttpGet(url);

		// Build query string
		URIBuilder ub = new URIBuilder(service.getURI());
		for (Entry<String, String> qa : arguments.entrySet()) {
			ub.addParameter(qa.getKey(), qa.getValue());
		}
		URI uri = ub.build();
		((HttpRequestBase) service).setURI(uri);

		// Execute get request
		HttpResponse response = httpclient.execute(service);
		return EntityUtils.toString(response.getEntity());
	}

	/**
	 * In the interests of simplicity, we assume only Form parameters are used, rather than Query (like with GET).
	 * @param url
	 * @param arguments
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String post(String url, Map<String, String> arguments) throws IllegalStateException, IOException, URISyntaxException {
		if (arguments == null) {
			arguments = new HashMap<String, String>();
		}
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost service= new HttpPost(url);

		// Build body
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (Entry<String, String> ba : arguments.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(ba.getKey(), ba.getValue()));
		}
		service.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// Execute post request
		HttpResponse response = httpclient.execute(service);
		return EntityUtils.toString(response.getEntity());
	}
	
	/**
	 * In the interests of simplicity, we assume only Query parameters are used, rather than Form (like with POST).
	 * @param url
	 * @param arguments
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String delete(String url, Map<String, String> arguments) throws URISyntaxException, ClientProtocolException, IOException {
		if (arguments == null) {
			arguments = new HashMap<String, String>();
		}
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpDelete service= new HttpDelete(url);
		// Build query string
		URIBuilder ub = new URIBuilder(service.getURI());
		for (Entry<String, String> qa : arguments.entrySet()) {
			ub.addParameter(qa.getKey(), qa.getValue());
		}
		URI uri = ub.build();
		((HttpRequestBase) service).setURI(uri);

		// Execute get request
		HttpResponse response = httpclient.execute(service);
		return EntityUtils.toString(response.getEntity());
	}
	
	/**
	 * In the interests of simplicity, we assume only Query parameters are used, rather than Form (like with POST).
	 * @param url
	 * @param arguments
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String put(String url, Map<String, String> arguments) throws URISyntaxException, ClientProtocolException, IOException {
		if (arguments == null) {
			arguments = new HashMap<String, String>();
		}
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPut service= new HttpPut(url);
		// Build query string
		URIBuilder ub = new URIBuilder(service.getURI());
		for (Entry<String, String> qa : arguments.entrySet()) {
			ub.addParameter(qa.getKey(), qa.getValue());
		}
		URI uri = ub.build();
		((HttpRequestBase) service).setURI(uri);

		// Execute get request
		HttpResponse response = httpclient.execute(service);
		return EntityUtils.toString(response.getEntity());
	}

}
