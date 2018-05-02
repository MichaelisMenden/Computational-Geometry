import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;


public class Main {
	private static TreeMap<Float, SimpleEntry<Integer, String>> eventQueue;
	private static ArrayList<Integer> SL = new ArrayList<Integer>();
	private static ArrayList<Integer> L = new ArrayList<Integer>();
	static ArrayList<Strecke> Strecken;
	
	
	public static <IntetsectingEdgeChecker> void main(String[] args) {
		eventQueue = initEventQueue();

		while(!eventQueue.isEmpty()) {
			Entry<Float, SimpleEntry<Integer, String>> E = eventQueue.firstEntry();
			if(E.getValue().getValue().equals("LEFT")) {
				treatLeftEndpoint(E);
			}
			if(E.getValue().getValue().equals("RIGHT")) {
				treatRightEndpoint(E);
			}
			if(E.getValue().getValue().equals("INTERESECT")) {
				treatIntersection(E);
			}		
			eventQueue.remove(eventQueue.firstEntry().getKey());
		}
		
		
		/*while(!eventQueue.isEmpty()) {
			System.out.println(eventQueue.firstEntry().getKey() + " " + eventQueue.firstEntry().getValue().getKey() + " " + eventQueue.firstEntry().getValue().getValue());
			eventQueue.remove(eventQueue.firstEntry().getKey());
		}*/
		
	}
	
	public static TreeMap<Float, SimpleEntry<Integer,String>> initEventQueue() {
		//Einträge Float = x (danach wird sortiert), Integer = Segementnummer, String = Links, Rechts oder Intersect
		TreeMap<Float, SimpleEntry<Integer,String>> eventQueue = new TreeMap<Float, SimpleEntry<Integer,String>>();
		IntersectingEdgeChecker iec = new IntersectingEdgeChecker();
		Strecken = iec.readFile(".\\src\\s_1000_10.dat");
		for(int i = 0; i < Strecken.size(); i++) {
			float xLeft = Strecken.get(i).getxStart();
			float xRight = Strecken.get(i).getxEnd();
			//Falls Linie in Datei mit Startpunkt weiter rechts angegeben ist -> tauschen
			if(xLeft > xRight) {
				xLeft = xRight;
				xRight = Strecken.get(i).getxStart();
			}
			
			eventQueue.put(xLeft,new SimpleEntry<Integer,String>(i,"LEFT"));
			eventQueue.put(xRight, new SimpleEntry<Integer,String>(i,"RIGHT"));
		}
		return eventQueue;
	}
	
	public static void treatLeftEndpoint(Entry<Float, SimpleEntry<Integer, String>> E) {
		//An richtige Stelle einfügen
		Strecke SegmE = Strecken.get(E.getValue().getKey());
		if(E.getValue().getValue() == "LEFT") {
			
		}
		
		
		System.out.println(E.getKey() + " " + E.getValue().getKey() + " " + E.getValue().getValue());
	}
	
	public static void treatRightEndpoint(Entry<Float, SimpleEntry<Integer, String>> E) {
		System.out.println(E.getKey() + " " + E.getValue().getKey() + " " + E.getValue().getValue());
	}
	
	public static void treatIntersection(Entry<Float, SimpleEntry<Integer, String>> E) {
		System.out.println(E.getKey() + " " + E.getValue().getKey() + " " + E.getValue().getValue());
	}
	
	
}
