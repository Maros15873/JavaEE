package components;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParserDovoleniek2 {
	
	public static void main(String[] args) {
		
		Integer sucet_dni = 0;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			Document nope = db.parse("dovolenky.xml");
			NodeList list_dovoleniek = nope.getElementsByTagName("dovolenka");
			
			String krajina = "";
			String miesto = "";
			String rok = "";
			Integer pocetdni = 0;
			
			for (int i = 0; i < list_dovoleniek.getLength(); i++) {
				NodeList dzeci = list_dovoleniek.item(i).getChildNodes();
				for (int j = 0; j < dzeci.getLength(); j++) {
					if (dzeci.item(j).getNodeName().equals("miesto")) {
						krajina = dzeci.item(j).getAttributes().getNamedItem("krajina").getNodeValue();
						miesto = dzeci.item(j).getTextContent();
					} else if (dzeci.item(j).getNodeName().equals("rok")) {
						rok = dzeci.item(j).getTextContent();
					} else if (dzeci.item(j).getNodeName().equals("pocetdni")) {
						pocetdni = Integer.parseInt(dzeci.item(j).getTextContent());
						sucet_dni += pocetdni;
					}
				}
				System.out.println(rok + ": " + miesto +" ("+krajina+")");
			}
			System.out.println("Súèet dní dovolenky: " + sucet_dni);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = null;
			try {
				transformer = transformerFactory.newTransformer();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        DOMSource source = new DOMSource(nope);
	        StreamResult result = new StreamResult(new FileOutputStream("output.xml"));
	        try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
