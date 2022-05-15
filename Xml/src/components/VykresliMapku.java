package components;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;

public class VykresliMapku {

	public static void main(String[] args) {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setValidating(false);
			SAXParser p = spf.newSAXParser();
			XMLReader parser = p.getXMLReader();
			parser.setErrorHandler(new VypisChyb());
			ParserMapiek d = new ParserMapiek();
			parser.setContentHandler(d);
			parser.parse("map.osm");
			d.vykresliMapku();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
