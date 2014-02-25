package realtimeweb.stickyweb.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.htmlcleaner.TagNode;
import org.junit.Test;

import realtimeweb.stickyweb.StickyWeb;
import realtimeweb.stickyweb.StickyWebResponse;

public class MainTest {

	@Test
	public void test() {
		StickyWeb sw = new StickyWeb();
		try {
			StickyWebResponse result = sw
					.get("http://www.sluggy.com", null).setOnline(true)
					.execute();
			TagNode test = result.asHTML();
			Object[] comicObj = result.asHTML().evaluateXPath(
					"//*[@id='container']/div[2]/div[1]/div[2]/img");
			System.out.println((TagNode) comicObj[0]);
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
