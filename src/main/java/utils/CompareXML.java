package utils;

import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class CompareXML {

	public boolean compare(String xml1, String xml2) {

		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);
		if(xml1 == null && xml2 == null)
			return true;

		Diff diff = null;
		try {
			diff = DiffBuilder.compare(xml1).withTest(xml2)
					.ignoreWhitespace()
					.normalizeWhitespace().ignoreComments()
					.checkForSimilar() 						// a different order is always 'similar' not equals.
					.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
					.build();
		}
		catch (Exception e) {

			return false;
		}
		return !diff.hasDifferences();
	}

	public static void main(String[] args) throws Exception {
		String result = "<geoPlugin>\n" +
				"<geoplugin_request>105.22.102.135</geoplugin_request>\n" +
				"<geoplugin_delay>2ms</geoplugin_delay>\n" +
				"<width>150</width>" +
				"<geoplugin_status>200</geoplugin_status>\n" +
				 "<image>\n" +
				"            <title>Yahoo! Weather</title>\n" +
				"            <width>142</width>" + "</image>" + "</geoPlugin>";
		String expected = "<geoPlugin>\n" +
				"<geoplugin_request>105.22.102.135</geoplugin_request>\n" +
				"<geoplugin_status>200</geoplugin_status>\n" +
				"<geoplugin_delay>2ms</geoplugin_delay>\n" +
				"<width>150</width>" +
				"<image>\n" +
				"            <title>Yahoo! Weather</title>\n" +
				"            <width>142</width>" + "</image>" + "</geoPlugin>";
		CompareXML xc = new CompareXML();
		//Assert.assertEquals(true, xc.compare(result, expected));
		System.out.println((assertXMLEquals(expected, result)));
	}

	public static boolean assertXMLEquals(String expectedXML, String actualXML)  {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setCoalescing(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(true);
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document doc1 = null, doc2 = null;
		try {
			doc1 = db.parse(new File("src/main/resources/XMLdata"));

		doc1.normalizeDocument();

		doc2 = db.parse(new File("src/main/resources/XMLdata1"));
		doc2.normalizeDocument();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return doc1.isEqualNode(doc2);
	}
}
