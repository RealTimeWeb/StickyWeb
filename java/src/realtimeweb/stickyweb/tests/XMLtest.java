package realtimeweb.stickyweb.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import realtimeweb.stickyweb.StickyWebResponse;

public class XMLtest {

	@Test
    public void testParse() {
        StickyWebResponse swr = new StickyWebResponse("<xml><test attr='5'>Banan</test></xml>");
        XPath xPath =  XPathFactory.newInstance().newXPath();
        try {
        	String attrVal = xPath.compile("/xml/test/@attr").evaluate(swr.asXML());
			System.out.println(attrVal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
