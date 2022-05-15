package sk.mm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class EeJson {
	
	public static void main(String args[]) {
		objectModelAPI();
		try {
			streamingAPI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void objectModelAPI() {
			
			String input_url = "https://data.statistics.sk/api/v2/dataset/og3010rr/SK01/2019/0?lang=en";
			
			
			try {
				URL url = new URL(input_url);
				InputStream is = url.openStream();
				JsonReader jsr = Json.createReader(is);
				JsonObject objectos = jsr.readObject();
				JsonObject jname = objectos.getJsonObject("dimension").getJsonObject("og3010rr_ukaz").getJsonObject("category").getJsonObject("label");
				JsonArray jcount = objectos.get("value").asJsonArray();
				int jedna_hviezdicka = 10000;
				
				int poc = 0;
				
				for (String jout : jname.keySet()) {
					String name = jname.get(jout).toString();
					String number = jcount.get(poc).toString();
					poc++;
					
					System.out.println(name + ": " + number + " " + "*".repeat(Math.round(Float.parseFloat(number) / jedna_hviezdicka)));
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

	}
	
	public static void streamingAPI() throws IOException {
	
		
		List array_names = new ArrayList<>();
		List array_values = new ArrayList<>();
		
		String input_url = "https://data.statistics.sk/api/v2/dataset/og3010rr/SK01/2019/0?lang=en";
		
		URL url = new URL(input_url);
		InputStream is = url.openStream();
		
		JsonParser parser = Json.createParser(is);
		
		while (parser.hasNext()) {
	          Event e = parser.next();
	          if (e == Event.KEY_NAME) {
	              switch (parser.getString()) {
	                  case "og3010rr_ukaz":	                	  
	                	  while (e != Event.KEY_NAME || !parser.getString().equals("category")) {
	                		  e = parser.next();
	                	  }
	                	  while (e != Event.KEY_NAME || !parser.getString().equals("label")) {
	                		  e = parser.next();
	                	  }
	                	  e = parser.next();
	                	  e = parser.next();
		                  while (e != Event.END_OBJECT) {
		                	 
		                	  e = parser.next();
		                	  array_names.add(parser.getString());
		                	  e = parser.next();
		                  }
	                      break;
	                case "value":
	                	e = parser.next();
	                	e = parser.next();
	                	while (e != Event.END_ARRAY) {
	                    	array_values.add(parser.getString());
	                    	e = parser.next();
	                    }
	                    break;
	             }
	         }
	     }
		
		int jedna_hviezdicka = 10000;
		
		int poc = 0;

		
		for (int i = 0; i < array_names.size(); i++) {
			System.out.println(array_names.get(i) + ": " + array_values.get(i) + " " + "*".repeat(Math.round(Float.parseFloat((String) array_values.get(i)) / jedna_hviezdicka)));
		}
		

	}

}
