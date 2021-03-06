package components;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.w3c.dom.Attr;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class ParserMapiek extends DefaultHandler {
	
	Map<Long, List <Float>> mapka = new HashMap<>();
	List<List <Long>> cesty = new ArrayList();
	
	List<Long> tmp_cesta = new ArrayList();
	
	boolean spracovavamWay = false;
	boolean spracovavamChodnicek = false;

  public void startDocument() 
  {
    
  }  

  public void startElement(String uri, String localName, String qName, Attributes atts) 
  {
    if (qName.equals("node") == true) 
    {
    	Long kluc = Long.parseLong(atts.getValue("id"));
    	Float lat = Float.parseFloat(atts.getValue("lat"));
    	Float lon = Float.parseFloat(atts.getValue("lon"));
    	mapka.put(kluc, Arrays.asList(lat, lon));
    } else if (qName.equals("way") == true) {
    	spracovavamWay = true;
    } else if (spracovavamWay == true && qName.equals("nd")) {
    	tmp_cesta.add(Long.parseLong(atts.getValue("ref")));
    } else if (spracovavamWay == true && qName.equals("tag")) {
    	if ((atts.getValue("k").equals("highway")) && (atts.getValue("v").equals("footway"))){
    		spracovavamChodnicek = true;
    	} 
    } 
  }    

  public void endElement(String uri, String localName, 
                        String qName) {
    if (qName.equals("way") == true) {
    	if (spracovavamChodnicek == true) {
    		cesty.add(tmp_cesta);
    	} 
    	tmp_cesta = new ArrayList();
    	spracovavamChodnicek = false;
    	spracovavamWay = false;
    }
    
  }    

  public void characters(char[] ch, int start, int length)  {
      

  }
  
  public void vykresliMapku() {
	  JFrame frame = new JFrame("FrameDemo");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      Draw emptyLabel = new Draw(mapka, cesty);
      emptyLabel.setPreferredSize(new Dimension(1200, 800));
      frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

      
   
            
      //Display the window.
      frame.pack();
      frame.setVisible(true);
      
      
  }
  
  
}
