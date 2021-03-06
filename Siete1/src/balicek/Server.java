//ZMENIT NA INE

package balicek;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Server {

	int port = 2004;
	Socket socket;

	ServerSocket srv;
	Map <Integer, InputStream> inputy_map = new HashMap<>();
	Map <Integer, OutputStream> outputy_map = new HashMap<>();
	Map <Integer, Integer> myputy = new HashMap<>();
	Map <Integer, String> menaputas = new HashMap<>();
	
	int message_type = 0;
	
	
	Map <Integer, InputStream> users_inputs = new HashMap<>();

	public Server() {
		try {
			srv = new ServerSocket(port);
			Thread thread = new Thread(()-> {
				accept();
			});
			thread.start();


		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public void accept() {
		InputStream rd;
		OutputStream wr;
			
		try {
			socket = srv.accept();
			
			wr = socket.getOutputStream();
			rd = socket.getInputStream();
			
			inputy_map.put(rd.hashCode(), rd);
			outputy_map.put(wr.hashCode(), wr);
			myputy.put(rd.hashCode(), wr.hashCode());
			
			
			Thread th = new Thread(() -> {
				
				
				while (prijmiSpravu(rd));
				
				inputy_map.remove(rd.hashCode());
				outputy_map.remove(wr.hashCode());
				myputy.remove(rd.hashCode());
				menaputas.remove(rd.hashCode());
				
				System.out.println("Koniec rozhovoru." + rd.hashCode() + " skoncil!");
				
				ObjectMapper obj_map = new ObjectMapper();
				ObjectNode slovnik = obj_map.createObjectNode();
				slovnik.put("sender_id", rd.hashCode());
				slovnik.put("type", 2); //delete user

				byte[] bts = slovnik.toString().getBytes();
				
				try {
					
					for (Map.Entry<Integer,OutputStream> output_map : outputy_map.entrySet()) {
						OutputStream output = output_map.getValue();
						output.write(bts.length & 255);
						output.write((bts.length >> 8) & 255);
						output.write((bts.length >> 16) & 255);
						output.write(bts.length >> 24);
						output.write(bts, 0, bts.length);
						output.flush();						
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//try {
				//	socket.close();
				//} catch (Exception e) {
				//	e.printStackTrace();
				//}
				//System.exit(0);
			});
			th.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		accept();
	}

	public boolean prijmiSpravu(InputStream rd) {
		try {
			
			//System.out.println(inputy_map);
			//System.out.println(outputy_map);
			//System.out.println(myputy);			
			
			int nbts = rd.read();
			int nbts2 = rd.read();
			int nbts3 = rd.read();
			int nbts4 = rd.read();
			if ((nbts < 0) || (nbts2 < 0))
				return false;
			nbts = nbts + (nbts2 << 8) + (nbts3 << 16) + (nbts4 << 32);
			byte bts[] = new byte[nbts];
			int i = 0; // how many bytes did we read so far
			do {
				int j = rd.read(bts, i, bts.length - i);
				if (j > 0)
					i += j;
				else
					break;
			} while (i < bts.length);
			
			String sprava_json = new String(bts);
			ObjectMapper objmap_json = new ObjectMapper();
			ObjectNode slovnik_json = (ObjectNode) objmap_json.readTree(sprava_json); 
			String message = slovnik_json.get("message").asText();
			String file = slovnik_json.get("file").asText();
			
			Integer what_type = slovnik_json.get("type").asInt();

			System.out.println(message);
			
			if (menaputas.containsKey(rd.hashCode()) == false) {
				//menaputas.put(rd.hashCode(), new String(bts));
				menaputas.put(rd.hashCode(), message);
				message_type = 1; //vytvorit nove meno
			} else {
				if (what_type == 91) {
					message_type = 91; //file posielanie
				} else {
					message_type = 0; //klasicka sprava
				}
				
			}
			
			ObjectMapper objmap = new ObjectMapper();
			ObjectNode slovnik = objmap.createObjectNode();
			slovnik.put("sender_id", rd.hashCode());
			slovnik.put("sender_name", menaputas.get(rd.hashCode()));
			slovnik.put("type", message_type);
			slovnik.put("message", message);
			slovnik.put("file", file);
			//slovnik.put("message", new String(bts));
			slovnik.put("users", menaputas.toString());
			String sprava = slovnik.toString();
			bts = sprava.getBytes();
			
			try {
				
				for (Map.Entry<Integer,OutputStream> output_map : outputy_map.entrySet()) {
					OutputStream output = output_map.getValue();
					if (!myputy.get(rd.hashCode()).equals(output_map.getKey())){						
						output.write(bts.length & 255);						
						output.write((bts.length >> 8) & 255);
						output.write((bts.length >> 16) & 255);
						output.write(bts.length >> 24);
						output.write(bts, 0, bts.length);
						output.flush();					
					} else {
						if (message_type == 1) {
							slovnik.put("type", -1);
							String sprava2 = slovnik.toString();
							byte[] bts_out = sprava2.getBytes();
							
							output.write(bts_out.length & 255);
							output.write((bts_out.length >> 8) & 255);
							output.write((bts_out.length >> 16) & 255);
							output.write(bts_out.length >> 24);
							output.write(bts_out, 0, bts_out.length);
							output.flush();	
						}
					}
	
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Server server = new Server();
	}

}