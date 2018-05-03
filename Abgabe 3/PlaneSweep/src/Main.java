import java.awt.geom.Point2D;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;


public class Main {
	//evtl Entry gegen Strecke tauschen da jetzt klar ist das start immer links ist
	private static TreeMap<Float, Event> eventQueue = new TreeMap<Float, Event>();
	private static ArrayList<SimpleEntry<Float, Strecke>> SL = new ArrayList<SimpleEntry<Float,Strecke>>();
	private static ArrayList<SimpleEntry<Point2D.Float,Event>> L = new ArrayList<SimpleEntry<Point2D.Float,Event>>();
	static ArrayList<Strecke> Strecken;
	private static IntersectingEdgeChecker iec = new IntersectingEdgeChecker();
	
	
	public static <IntetsectingEdgeChecker> void main(String[] args) {
		initEventQueue();
		eventQueue.firstEntry().getValue().doEvent();

		while(!eventQueue.isEmpty()) {
			Entry<Float, Event> E = eventQueue.firstEntry();
			if(E.getKey() == E.getValue().strecke.getxStart()) { //bedeutet linker Anfangspunkt
				E.getValue().doEvent();
			}
			if(E.getKey() == E.getValue().strecke.getxEnd()) {	//bedeutet rechter Endpunkt
				E.getValue().doEvent();
			}
			if(E.getKey() != E.getValue().strecke.getxStart() && E.getKey() != E.getValue().strecke.getxEnd()) {
				E.getValue().doEvent();
			}		
			eventQueue.remove(eventQueue.firstEntry().getKey());
		}		
	}
	
	public static void initEventQueue() {
		Strecken = iec.readFile(".\\src\\s_1000_10.dat");
		for(int i = 0; i < Strecken.size(); i++) {
			Strecke strecke = Strecken.get(i);
			float xLeft = strecke.getxStart();
			float xRight = strecke.getxEnd();
			
			eventQueue.put(xLeft, new Event(strecke) {
				public void doEvent() {
					treatLeftEndpoint(new SimpleEntry<Float,Strecke>(xLeft,strecke));
				}
			});
			eventQueue.put(xLeft, new Event(strecke) {
				public void doEvent() {
					treatRightEndpoint(new SimpleEntry<Float,Strecke>(xRight,strecke));
				}
			});
			eventQueue.firstEntry().getValue().doEvent();
		}
	}
	
	public static void treatLeftEndpoint(SimpleEntry<Float,Strecke> e) {
		Strecke[] s = addToSL(e);
		final Point2D.Float intersectionPoint1 = iec.getIntersectionPoint((Strecke) e.getValue(), s[0]);
		if(intersectionPoint1 != null) {
			eventQueue.put((float) intersectionPoint1.getX(), new Event((Strecke) e.getValue(), s[0]) {
				public void doEvent() {
					treatIntersection(intersectionPoint1, strecke, strecke2);
				}
			});
		}
		final Point2D.Float intersectionPoint2 = iec.getIntersectionPoint((Strecke) e.getValue(), s[1]);
		if(intersectionPoint2 != null) {
			eventQueue.put((float) intersectionPoint2.getX(),new Event((Strecke) e.getValue(), s[1]) {
				public void doEvent() {
					treatIntersection(intersectionPoint2, strecke, strecke2);
				}
			});
		}
	}
		
		private static Strecke[] addToSL(Entry e) {
			Strecke segmE = (Strecke) e.getValue();
			Strecke SegmA = null, SegmB = null;
			for (int i = 0; i < SL.size(); i++) {
				SegmA = SL.get(i).getValue();
				SegmB = SL.get(i+1).getValue();  //später noch auf null testen
				
				//Falls SegmentA und SegmentB beides Anfangspunkte sind vergleiche mit Start Y der Strecken
				if(SL.get(i).getKey() == SegmA.getxStart() && SL.get(i+1).getKey() == SegmB.getxStart()) {
					if(segmE.getyStart() >= SegmA.getyStart() && segmE.getyStart() < SegmB.getyStart()
							|| segmE.getyStart() == SegmA.getxStart()) {
						SL.add(new SimpleEntry<Float,Strecke>((Float) e.getKey(), segmE));
						break;
					}
				}
				//Falls SegmentA Anfangspunkt und SegmentB Endpunkt ist vergleiche mit Start und Endpunkt der Strecken
				if(SL.get(i).getKey() == SegmA.getxStart() && SL.get(i+1).getKey() == SegmB.getxEnd()) {
					if(segmE.getyStart() >= SegmA.getyStart() && segmE.getyStart() < SegmB.getyEnd()
							|| segmE.getyStart() == SegmA.getxStart()) {
						SL.add(new SimpleEntry<Float,Strecke>((Float) e.getKey(), segmE));
						break;
					}
				}
				//Falls SegmentA Endpunkt und SegmentB Anfangspunkt ist vergleiche Mit End und Startpunkt der Strecken
				if(SL.get(i).getKey() == SegmA.getxEnd() && SL.get(i+1).getKey() == SegmB.getxStart()) {
					if(segmE.getyStart() >= SegmA.getyEnd() && segmE.getyStart() < SegmB.getyStart()
							|| segmE.getyStart() == SegmA.getyEnd()) {
						SL.add(new SimpleEntry<Float,Strecke>((Float) e.getKey(), segmE));
						break;
					}
				}
				//Falls SegmentA und SegmentB Endpunkt ist vergleiche mit Endpunkten der Strecke
				if(SL.get(i).getKey() == SegmA.getxEnd() && SL.get(i+1).getKey() == SegmB.getxEnd()) {
					if(segmE.getyStart() >= SegmA.getyEnd() && segmE.getyStart() < SegmB.getyEnd()
							|| segmE.getyStart() == SegmA.getyEnd()) {
						SL.add(new SimpleEntry<Float,Strecke>((Float) e.getKey(), segmE));
						break;
					}
				}
			}
			return new Strecke[] {SegmA,SegmB};
	}

	
	public static void treatRightEndpoint(SimpleEntry<Float,Strecke> e) {
		Strecke segmE = (Strecke) e.getValue();
		int index = findStreckeInSweepLine(segmE);
		Strecke segmA = SL.get(index - 1).getValue();
		Strecke segmB = SL.get(index + 1).getValue();
		SL.remove(index);
		Point2D.Float intersectionPoint = iec.getIntersectionPoint(segmA, segmB);
		if(intersectionPoint != null) {		//Wenn sie sich schneiden
			Event queueEntry = eventQueue.get(intersectionPoint.getX());
			if(queueEntry == null && !(queueEntry.strecke.equals(segmA) && queueEntry.strecke2.equals(segmB) || queueEntry.strecke.equals(segmB) && queueEntry.strecke2.equals(segmA))) {   //Wenn das Intersection Event noch nicht in der EventQueue ist
				eventQueue.put((float) intersectionPoint.getX(), new Event(segmA, segmB) {
					public void doEvent() {
						treatIntersection(intersectionPoint,this);
					}
				});
			}	
		}
	}
	
	public static int findStreckeInSweepLine(Strecke s) {
		for(int i = 0; i < SL.size(); i++) {
			if(s.equals(SL.get(i).getValue()))
				return i;
		}
		return -1;
	}
	
	public static void treatIntersection(Point2D.Float intersectionPoint, Event e) {
		L.add(new SimpleEntry<Point2D.Float,Event>(intersectionPoint, e));
		
	}
	
	
}
