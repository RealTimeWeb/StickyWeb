package realtimeweb.stickyweb.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.junit.Test;

import realtimeweb.stickyweb.StickyWeb;
import realtimeweb.stickyweb.StickyWebResponse;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebInternetException;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidPostArguments;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidQueryString;
import realtimeweb.stickyweb.exceptions.StickyWebNotInCacheException;
import realtimeweb.stickyweb.exceptions.StickyWebXMLResponseParseException;

public class MainTest {

	@Test
	public void test() {
		StickyWeb sw = new StickyWeb();
			StickyWebResponse result;
			try {
				result = sw
						.get("http://www.sluggy.com", null).setOnline(true)
						.execute();
				TagNode test = result.asHTML();
				Object[] comicObj;
				comicObj = result.asHTML().evaluateXPath(
						"//*[@id='container']/div[2]/div[1]/div[2]/img");
				System.out.println((TagNode) comicObj[0]);
			} catch (StickyWebNotInCacheException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (StickyWebInternetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (StickyWebInvalidQueryString e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (StickyWebInvalidPostArguments e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (StickyWebDataSourceNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (StickyWebXMLResponseParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XPatherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}

}
