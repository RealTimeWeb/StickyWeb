package realtimeweb.stickyweb.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.htmlcleaner.TagNode;
import org.junit.Test;

import realtimeweb.stickyweb.StickyWeb;
import realtimeweb.stickyweb.StickyWebResponse;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebInternetException;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidPostArguments;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidQueryString;
import realtimeweb.stickyweb.exceptions.StickyWebJsonResponseParseException;
import realtimeweb.stickyweb.exceptions.StickyWebNotInCacheException;

public class Oauth1Test {

	@Test
	public void test() {
		StickyWeb sw = new StickyWeb();
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("location", "blacksburg");
			StickyWebResponse result;
			try {
				result = sw
						.get("http://api.yelp.com/v2/search", parameters).setOnline(true)
						.execute();
				System.out.println(result.asJSON());
			} catch (StickyWebNotInCacheException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StickyWebInternetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StickyWebInvalidQueryString e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StickyWebInvalidPostArguments e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StickyWebDataSourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StickyWebJsonResponseParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
