package realtimeweb.stickyweb.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Document;

import com.sun.org.apache.xpath.internal.NodeSet;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import realtimeweb.stickyweb.StickyWebResponse;

public class XMLtest {

	@Test
    public void testParse() {
        StickyWebResponse swr = new StickyWebResponse("<xml><test attr='5'>Banan</test><test></test></xml>");
        XPath xPath =  XPathFactory.newInstance().newXPath();
        try {
        	Node test = (Node) xPath.evaluate("xml/test[1]", swr.asXML(), XPathConstants.NODE);
        	NodeSet test2 = (NodeSet) xPath.evaluate("xml/test[1]", swr.asXML(), XPathConstants.NODESET);
        	//String attrVal = xPath.compile("/xml/test/@attr").evaluate(swr.asXML());
        	//Node k = swr.asXML();
        	System.out.println(xPath.evaluate("@attr", test, XPathConstants.NODE));
			//System.out.println(attrVal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
