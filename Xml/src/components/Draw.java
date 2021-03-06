package components;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

public class Draw extends JPanel {
	
	Map<Long, List <Float>> mapka;
	List<List <Long>> cesty;
	
	
	
	public Draw(Map<Long, List<Float>> mapka, List<List<Long>> cesty) {
		super();
		this.mapka = mapka;
		this.cesty = cesty;
	}



	public void paint(Graphics g) {
		
		int min_x = Math.round(mapka.values().stream().map(x-> x.get(0)*-1).min((a,b)->Float.compare(a, b)).orElse((float) 0.0)*20000);
		int min_y = Math.round(mapka.values().stream().map(x-> x.get(1)*1).min((a,b)->Float.compare(a, b)).orElse((float) 0.0)*20000);
		
		for (List<Long> list : cesty) {
			g.drawPolyline(
					 list.stream().map(x-> Math.round(mapka.get(x).get(0)*-20000-min_x)).mapToInt(Integer::intValue).toArray(), 
					 list.stream().map(x-> Math.round(mapka.get(x).get(1)*20000-min_y)).mapToInt(Integer::intValue).toArray(),
					 list.stream().map(x-> Math.round(mapka.get(x).get(0))).mapToInt(Integer::intValue).toArray().length);
		}
		//g.drawLine(20, 20, 200, 180);
	}

	
	
	public static void main(String[] args) {
	
	}
}