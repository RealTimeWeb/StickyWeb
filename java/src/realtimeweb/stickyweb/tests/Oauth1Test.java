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

public class Oauth1Test {

	@Test
	public void test() {
		StickyWeb sw = new StickyWeb();
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("location", "blacksburg");
			StickyWebResponse result = sw
					.get("http://api.yelp.com/v2/search", parameters).setOnline(true)
					.execute();
			System.out.println(result.asJSON());
			// System.out.println((TagNode)comicObj);
			// System.out.println(((org.htmlcleaner.TagNode)comicObj).getAttributeByName("src"));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
