//ZMENIT NA INE

package balicek;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.control.Button;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.imageio.stream.FileImageInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;

public class VravFx extends Application implements Runnable {
	private static final long serialVersionUID = -4297335882692216363L;

	Socket socket;
	TextArea t1 = new TextArea();
	//TextArea t2 = new TextArea();
	
	Button btn = new Button();
	File file;
	
	private static final int port = 2004;
	VBox root;
	Stage stage;
	
	OutputStream wr;
	InputStream rd;
	
	String user_name;
	Integer user_id;
	
	Map <Integer, TextArea> text_areas = new HashMap<>();
	Map <Integer, Label> label_areas = new HashMap<>();
	Map <Integer, String> friends_names = new HashMap<>();
	
	
	
		
	@Override
	public void start(Stage primaryStage) {
		
		root = new VBox();
		this.stage = primaryStage;
		
		
		
		root.getChildren().add(t1);
		//root.getChildren().add(t2);
	    
		Scene scene = new Scene(root, 800, 600);
		primaryStage.setTitle("VravFx");
		primaryStage.setScene(scene);
		
        primaryStage.show();
        
        TextInputDialog dialog = new TextInputDialog();  // create an instance

        dialog.setTitle("Zadaj meno Johane");
        // other formatting etc

        Optional<String> result = dialog.showAndWait();  
        // this shows the dialog, waits until it is closed, and stores the result 
        
        user_name = result.orElse("Anonym");
        
        root.getChildren().add(btn);
		btn.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Odosli subor");
			file = fileChooser.showOpenDialog(stage);		
			byte[] bts = new byte[(int)file.length()];
			try {
				FileImageInputStream nfis = new FileImageInputStream(file);
				nfis.read(bts);
				nfis.close();
				
				String string = Base64.encodeBase64String(bts);
				
				ObjectMapper objmap_client = new ObjectMapper();
				ObjectNode slovnik_client = objmap_client.createObjectNode();
				slovnik_client.put("sender_id", user_id);
				slovnik_client.put("sender_name", user_name);
				slovnik_client.put("type", 91);
				slovnik_client.put("message", file.getName());
				slovnik_client.put("file", string);
				slovnik_client.put("users", "[]");
				
				String sprava_client = slovnik_client.toString();
				posliSpravu(sprava_client);
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

 
		t1.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		t1.requestFocus();
		t1.setOnKeyReleased(x -> {
			
			String sprava = t1.getText();
			
			sprava = sprava.replace(":)", "????");
			sprava = sprava.replace("xD", "????");
			sprava = sprava.replace(":(", "????");
			sprava = sprava.replace(":D", "????");
			
			t1.setText(sprava);
			t1.positionCaret(sprava.length());
			
			ObjectMapper objmap_client = new ObjectMapper();
			ObjectNode slovnik_client = objmap_client.createObjectNode();
			slovnik_client.put("sender_id", user_id);
			slovnik_client.put("sender_name", user_name);
			slovnik_client.put("type", 90);
			slovnik_client.put("message", sprava);
			slovnik_client.put("file", "");
			slovnik_client.put("users", "[]");
			
			String sprava_client = slovnik_client.toString();
			posliSpravu(sprava_client);
			//posliSpravu(sprava);
		});
		//t2.setEditable(false);
		stage.setOnCloseRequest((evt) -> {
	      try {
  		  socket.close();
		} catch (Exception e) { e.printStackTrace(); }
		});
		
		Thread th = new Thread(this);
		th.start();
		
	}

	public void run() 
	{
		createConnection();
		
		
		ObjectMapper objmap_client = new ObjectMapper();
		ObjectNode slovnik_client = objmap_client.createObjectNode();
		slovnik_client.put("sender_id", user_id);
		slovnik_client.put("sender_name", user_name);
		slovnik_client.put("type", 90);
		slovnik_client.put("message", user_name);
		slovnik_client.put("file", "");
		slovnik_client.put("users", "[]");
		
		String sprava_client = slovnik_client.toString();
		posliSpravu(sprava_client);
		//posliSpravu(user_name);

		while(prijmiSpravu());
		
		//System.out.println("Koniec rozhovoru.");
		try {
  		  //socket.close();
		} catch (Exception e) { e.printStackTrace(); }
		//System.exit(0);
	}

	private void createConnection() {
		try {

			try {
				// first try to connect to other party, if it is already listening
				socket = new Socket("localhost", port);
				System.out.println("Vytvoreny socket pre odosielanie");
			} catch (ConnectException e) {

			}
			wr = socket.getOutputStream();
			rd = socket.getInputStream();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void posliSpravu(String message) {
		byte bts[] = message.getBytes();
		    try {
			    wr.write(bts.length & 255);
			    wr.write((bts.length >> 8) & 255);
			    wr.write((bts.length >> 16) & 255);
			    wr.write(bts.length >> 24);
			    wr.write(bts, 0, bts.length);
			    wr.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public boolean prijmiSpravu() {
		try {
			int nbts = rd.read();			
			int nbts2 = rd.read();
			int nbts3 = rd.read();
			int nbts4 = rd.read();
			if ((nbts < 0) || (nbts2 < 0)) return false;
            nbts = nbts + ( nbts2 << 8) + (nbts3 << 16) + (nbts4 << 24);
			byte bts[] = new byte[nbts];
			int i = 0; // how many bytes did we read so far
			do {
				int j = rd.read(bts, i, bts.length - i);
				if (j > 0) i += j;
				else break;
			} while (i < bts.length);
			
			
			//t2.setText(new String(bts));
			String sprava = new String(bts);
			ObjectMapper objmap = new ObjectMapper();
			ObjectNode slovnik = (ObjectNode) objmap.readTree(sprava); 
			
			
			if (slovnik.get("type").asInt() == 1) {
				Platform.runLater(()->{
					Label label = new Label(slovnik.get("sender_name").asText());
					label_areas.put(slovnik.get("sender_id").asInt(), label);
					root.getChildren().add(label_areas.get(slovnik.get("sender_id").asInt()));
					text_areas.put(slovnik.get("sender_id").asInt(), new TextArea());
					root.getChildren().add(text_areas.get(slovnik.get("sender_id").asInt()));
					text_areas.get(slovnik.get("sender_id").asInt()).setEditable(false);
				});
			} else if (slovnik.get("type").asInt() == -1) {
				
				Platform.runLater(()->{
					user_id = slovnik.get("sender_id").asInt();
					friends_names = vytvor_mapu(slovnik.get("users").asText());
					for (Map.Entry<Integer,String> user : friends_names.entrySet()) {
						Integer kluc = user.getKey();
						String value = user.getValue();
						if (kluc.equals(user_id) == false) {
							//System.out.println(kluc + " -- " + user_id);
							Label label = new Label(value);
							label_areas.put(kluc, label);
							root.getChildren().add(label_areas.get(kluc));
							text_areas.put(kluc, new TextArea());
							root.getChildren().add(text_areas.get(kluc));
							//text_areas.get(kluc).setPromptText("MADAFAKA");
							
							text_areas.get(kluc).setEditable(false);
						} 
						
					}
				});
			} else if (slovnik.get("type").asInt() == 0) {
				if (text_areas.containsKey(slovnik.get("sender_id").asInt()) == true) {
					//text_areas.get(slovnik.get("sender_id").asInt()).setText(slovnik.get("message").asText());
					text_areas.get(slovnik.get("sender_id").asInt()).setText(slovnik.get("message").asText());
				}
			} else if (slovnik.get("type").asInt() == 2) {
				Platform.runLater(()->{
					int id2remove = slovnik.get("sender_id").asInt();
					root.getChildren().remove(text_areas.get(id2remove));
					root.getChildren().remove(label_areas.get(id2remove));
					text_areas.remove(slovnik.get("sender_id").asInt());
					label_areas.remove(slovnik.get("sender_id").asInt());
					friends_names.remove(slovnik.get("sender_id").asInt());					
				});
			} else if (slovnik.get("type").asInt() == 91) {
				byte[] bts_file = Base64.decodeBase64(slovnik.get("file").asText());
				File new_file = new File(slovnik.get("message").asText());
				FileOutputStream fos = new FileOutputStream(new_file);
				fos.write(bts_file);
			}
			
			//t2.setText(slovnik.get("message").asText());
			
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public static Map <Integer, String> vytvor_mapu(String users_names) {
		Map <Integer, String> names_map = new HashMap<>();
		users_names = users_names.substring(1,users_names.length() - 1);
		String[] arrOfStr = users_names.split(",", 0);
		for (String string : arrOfStr) {
			String[] tmp = string.split("=", 0);
			names_map.put(Integer.parseInt(tmp[0].strip()),tmp[1].strip());
		}
		System.out.println(names_map.toString());
		return names_map;
	}

	public static void main(String[] args) {
		launch(args);
	}
}