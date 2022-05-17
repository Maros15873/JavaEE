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
	private static final long serialVersionUID = 1L;
	private ObjectMapper om = new ObjectMapper();
	private static int ids = 0;
       
 
    public Chat() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		checkUsers(request, response);
		Map <Integer,String> users = (Map<Integer, String>) request.getServletContext().getAttribute("users");
		List <String> messages = (List<String>) request.getServletContext().getAttribute("messages");
		int skip = (int) request.getSession().getAttribute("skip");
		
		//ODOSLEM NOVU SPRAVU UZIVATELOM
		ObjectNode on = om.createObjectNode();
		JsonNode lasdjfoisdj = om.valueToTree(messages.subList(skip, messages.size()));
		on.put("messages", lasdjfoisdj);
		lasdjfoisdj = om.valueToTree(users.values());
		on.put("users", lasdjfoisdj);

		//ODPOVED SERVERA
		response.getWriter().append(on.toString());
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader brr = request.getReader();
		JsonNode json = om.readTree(brr);
		int aktId;
		
		//INICIALIZUJEM STORAGE
		if (request.getServletContext().getAttribute("users") == null) {
			request.getServletContext().setAttribute("users", new HashMap<Integer, String>());
			request.getServletContext().setAttribute("messages", new ArrayList<String>());
			request.getServletContext().setAttribute("time", new HashMap<Integer, Date>());
		}
		
		List <String> messages = (List<String>) request.getServletContext().getAttribute("messages");
		Map <Integer,String> users = (Map<Integer, String>) request.getServletContext().getAttribute("users");
		Map <Integer,Date> time = (Map<Integer, Date>) request.getServletContext().getAttribute("time");
		
		//ZISTIM, CI MA UZIVATEL ID 
		if (request.getSession().getAttribute("id") == null) {
			aktId = ids++;
			request.getSession().setAttribute("id", aktId);
			request.getSession().setAttribute("skip", 0);
			request.getSession().setAttribute("name", json.get("name").asText());
			
			time.put(aktId, new Date());
			
			//pridam do globalenho zoznamu mien
			users.put(aktId, json.get("name").asText());
			return;
		}
		
		checkUsers(request, response);
		aktId = (int) request.getSession().getAttribute("id");
		
		if (!users.containsKey(aktId)) {
			users.put(aktId, (String) request.getSession().getAttribute("name")); 
		}
		
		time.put(aktId, new Date());
		int skip = (int) request.getSession().getAttribute("skip");
		String userName = users.get(aktId);
		
		System.out.println(users);
		//CITAT POST A PRIDAM POSLEDNU SPRAVU DO SPRAV NA GLOBAL ULOZISKU
		String message = json.get("message").asText();
		messages.add(userName + ": " + message);
		
		//ODOSLEM NOVU SPRAVU UZIVATELOM
		ObjectNode on = om.createObjectNode();
		
		
		JsonNode lasdjfoisdj = om.valueToTree(messages.subList(skip, messages.size()));
		on.put("messages", lasdjfoisdj);
		lasdjfoisdj = om.valueToTree(users.values());
		on.put("users", lasdjfoisdj);
		
		//ODPOVED SERVERA
		response.getWriter().append(on.toString());
		
		
		
		/*
		//GLOBALNE ULOZISKO INFORMACII
		request.getServletContext().setAttribute("chat", "Maros");
		request.getServletContext().getAttribute("name");
		
		//SESSION MA INFO LEN O PRIRADENOM USEROVI
		request.getSession().setAttribute("chat", "Maros");
		request.getSession().getAttribute("chat");*/
		
		
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map <Integer,String> users = (Map<Integer, String>) request.getServletContext().getAttribute("users");
		int aktId = (int) request.getSession().getAttribute("id");
		users.remove(aktId);
		request.getSession().setAttribute("id", null);
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("skip", ((List<String>) request.getServletContext().getAttribute("messages")).size());
	}
	
	protected void checkUsers (HttpServletRequest request, HttpServletResponse response) {
		Map <Integer,Date> time = (Map<Integer, Date>) request.getServletContext().getAttribute("time");
		Map <Integer,String> users = (Map<Integer, String>) request.getServletContext().getAttribute("users");
		
		Date actualTime = new Date();
		
		for (Entry<Integer, Date> itr : time.entrySet()) {
			if (actualTime.getTime() - itr.getValue().getTime() > 30*1000) {
				users.remove(itr.getKey());	
			}
		}
	}

}
