package balicek;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@WebServlet("/Chat")
public class Chat extends HttpServlet {
	private static int ids = 0;
       
    public Chat() {
        super();        
    }
    
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("skip", ((List<String>) request.getServletContext().getAttribute("messages")).size());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		Map <Integer,Date> time = (Map<Integer, Date>) request.getServletContext().getAttribute("time");
		Map <Integer,String> users = (Map<Integer, String>) request.getServletContext().getAttribute("users");		
		Date actualTime = new Date();	
		for (Entry<Integer, Date> itr : time.entrySet()) {
			if (actualTime.getTime() - itr.getValue().getTime() > 60*1000) {
				users.remove(itr.getKey());	
			}
		}
		List <String> messages = (List<String>) request.getServletContext().getAttribute("messages");
		int skip = (int) request.getSession().getAttribute("skip");
		JsonNode sprava;
		ObjectMapper obj_map = new ObjectMapper();
		ObjectNode slovnik = obj_map.createObjectNode();
		sprava = obj_map.valueToTree(messages.subList(skip, messages.size()));
		slovnik.put("messages", sprava);
		sprava = obj_map.valueToTree(users.values());
		slovnik.put("users", sprava);		
		response.getWriter().append(slovnik.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader brr = request.getReader();
		ObjectMapper obj_map = new ObjectMapper();
		JsonNode sprava = obj_map.readTree(brr);
		int user_id;
		if (request.getServletContext().getAttribute("users") == null) {
			request.getServletContext().setAttribute("users", new HashMap<Integer, String>());
			request.getServletContext().setAttribute("messages", new ArrayList<String>());
			request.getServletContext().setAttribute("time", new HashMap<Integer, Date>());
		}		
		List <String> messages = (List<String>) request.getServletContext().getAttribute("messages");
		Map <Integer,String> users = (Map<Integer, String>) request.getServletContext().getAttribute("users");
		Map <Integer,Date> time = (Map<Integer, Date>) request.getServletContext().getAttribute("time");
		if (request.getSession().getAttribute("id") == null) {
			user_id = ids;
			ids += 1;
			request.getSession().setAttribute("id", user_id);
			request.getSession().setAttribute("skip", 0);
			request.getSession().setAttribute("name", sprava.get("name").asText());			
			time.put(user_id, new Date());
			users.put(user_id, sprava.get("name").asText());
		} else {		
			Date actualTime = new Date();		
			for (Entry<Integer, Date> itr : time.entrySet()) {
				if (actualTime.getTime() - itr.getValue().getTime() > 60*1000) {
					users.remove(itr.getKey());	
				}
			}		
			user_id = (int) request.getSession().getAttribute("id");			
			if (!users.containsKey(user_id)) {
				users.put(user_id, (String) request.getSession().getAttribute("name")); 
			}			
			time.put(user_id, new Date());
			int skip = (int) request.getSession().getAttribute("skip");
			String userName = users.get(user_id);
			String message = sprava.get("message").asText();
			messages.add(userName + ": " + message);			
			ObjectNode slovnik = obj_map.createObjectNode();			
			JsonNode new_sprava; 
			new_sprava = obj_map.valueToTree(messages.subList(skip, messages.size()));
			slovnik.put("messages", new_sprava);
			new_sprava = obj_map.valueToTree(users.values());
			slovnik.put("users", new_sprava);
			response.getWriter().append(slovnik.toString());
		}	
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map <Integer,String> users = (Map<Integer, String>) request.getServletContext().getAttribute("users");
		int user_id = (int) request.getSession().getAttribute("id");
		users.remove(user_id);
		request.getSession().setAttribute("id", null);
	}
}
