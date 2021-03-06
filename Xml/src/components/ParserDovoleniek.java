package components;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class ParserDovoleniek extends DefaultHandler 
{
  private StringBuffer rok = new StringBuffer(10);
  private StringBuffer miesto = new StringBuffer(50);
  private StringBuffer pocet_dni = new StringBuffer(10);
  private String krajina;
  private boolean spracovavamRok, spracovavamMiesto, spracovavamPocetDni;
  private TreeMap<Integer,String> dovolenky;
  
  private Integer sucet_dni = 0;

  public void vypisDovolenky()
  {
    for (Iterator<Map.Entry<Integer,String>> i = dovolenky.entrySet().iterator(); i.hasNext(); )
    {
      Map.Entry<Integer,String> v = i.next();
      System.out.println(v.getKey() + ": " + v.getValue());
    }
    System.out.println("S??et dn? dovolenky: " + sucet_dni);
  }

  public void startDocument() 
  {
    dovolenky = new TreeMap<Integer,String>();
  }

  public void startElement(String uri, String localName, String qName, Attributes atts) 
  {
    if (qName.equals("rok") == true) 
    {
      spracovavamRok = true;
      rok.setLength(0);
    }
    else if (qName.equals("miesto") == true)
    {
      spracovavamMiesto = true;
      krajina = atts.getValue("krajina");
      miesto.setLength(0);
    }
    else if (qName.equals("pocetdni") == true) {
    	spracovavamPocetDni = true;
    }
  }    

  public void endElement(String uri, String localName, 
                        String qName) {
    if (qName.equals("rok") == true) 
    {
      spracovavamRok = false; 
    }
    else if (qName.equals("miesto") == true)
    {
      spracovavamMiesto = false;
    }
    else if (qName.equals("dovolenka") == true) 
    {
      miesto.append(" (");
      miesto.append(krajina);
      miesto.append(")");

      dovolenky.put(new Integer(Integer.parseInt(rok.toString())), miesto.toString());
    }
    else if (qName.equals("pocetdni") == true) 
    {
      spracovavamPocetDni = false; 
      sucet_dni += Integer.parseInt(pocet_dni.toString());
      pocet_dni = new StringBuffer(10);
    }
  }    

  public void characters(char[] ch, int start, int length) 
  {
    if (spracovavamRok == true) 
    {  
      rok.append(ch, start, length);
    }        
    else if (spracovavamMiesto == true){ 
      miesto.append(ch, start, length);
    }
    else if (spracovavamPocetDni == true)
    { 
      pocet_dni.append(ch, start, length);
    }
  }
}
