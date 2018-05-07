import java.awt.geom.Point2D;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;


public class Main {
	//evtl Entry gegen Strecke tauschen da jetzt klar ist das start immer links ist
	private static TreeMap<Float, Event> eventQueue = new TreeMap<Float, Event>();
	private static ArrayList<Strecke> SL = new ArrayList<Strecke>();
	private static ArrayList<SimpleEntry<Point2D.Float,Event>> L = new ArrayList<SimpleEntry<Point2D.Float,Event>>();
	static ArrayList<Strecke> Strecken;
	private static IntersectingEdgeChecker iec = new IntersectingEdgeChecker();
	private static Strecke SLStrecke = new Strecke(0,-10000,0,10000);
	
	
	public static <IntetsectingEdgeChecker> void main(String[] args) {
		initEventQueue();
		long start = System.currentTimeMillis();
		while(!eventQueue.isEmpty()) {
			Entry<Float, Event> E = eventQueue.firstEntry();
			float x = E.getKey();

			E.getValue().doEvent();
						
			eventQueue.remove(eventQueue.firstEntry().getKey());
			System.out.println(eventQueue.size());
			//System.out.println(L.size());
		}
		long end = System.currentTimeMillis();
		System.out.println(end-start + " ms");
		doOutput();
	}
	
	public static void initEventQueue() {
		Strecken = iec.readFile(".\\src\\s_1000_10.dat");
		for(int i = 0; i < Strecken.size(); i++) {
			Strecke strecke = Strecken.get(i);
			float xLeft = strecke.getxStart();
			float xRight = strecke.getxEnd();
			char a = 'a';
			float[] keys = keyCollision(xLeft,xRight);
			eventQueue.put(keys[0], new Event(strecke) {
				public void doEvent() {
					treatLeftEndpoint(new SimpleEntry<Float,Strecke>(keys[0],strecke));
				}
			});
			eventQueue.put(keys[1], new Event(strecke) {
				public void doEvent() {
					treatRightEndpoint(new SimpleEntry<Float,Strecke>(keys[1],strecke));
				}
			});
		}
	}
	private static float[] keyCollision(float xLeft, float xRight) {
		if(eventQueue.containsKey(xLeft))
			xLeft += .0001;
		if(eventQueue.containsKey(xRight))
			xRight -= .0001;
		return new float[]{xLeft,xRight};
	
	}
	
	public static void treatLeftEndpoint(SimpleEntry<Float,Strecke> e) {
		Strecke[] s = addToSL(e.getValue());
		
		if(s[0] != null) {
			final Point2D.Float intersectionPoint1 = iec.getIntersectionPoint((Strecke) e.getValue(), s[0]);
			insertIntersection(intersectionPoint1,(Strecke) e.getValue(),s[0]);
		}
		if(s[1] != null) {
			final Point2D.Float intersectionPoint2 = iec.getIntersectionPoint((Strecke) e.getValue(), s[1]);
			insertIntersection(intersectionPoint2,(Strecke) e.getValue(),s[1]);
		}
	}
		
		private static Strecke[] addToSL(Strecke segmE) {
			Strecke SegmA = null, SegmB = null;
			SLStrecke.setBothX(segmE.getxStart());
			
			if(SL.size() == 0) {	//Falls SL leer ist
				SL.add(segmE);
				return new Strecke[]{null,null};
			}
			
			for (int i = 0; i < SL.size() - 1; i++) {
				SegmA = SL.get(i);
				SegmB = SL.get(i+1);  		
				Point2D.Float pA = iec.getIntersectionPoint(SegmA, SLStrecke);
				Point2D.Float pB = iec.getIntersectionPoint(SegmB, SLStrecke);
				Point2D.Float pE = iec.getIntersectionPoint(segmE, SLStrecke);
						
				if((iec.getIntersectionPoint(SegmA, SLStrecke).getY() >= iec.getIntersectionPoint(segmE, SLStrecke).getY() && iec.getIntersectionPoint(SegmB, SLStrecke).getY() < iec.getIntersectionPoint(segmE, SLStrecke).getY())
					|| iec.getIntersectionPoint(SegmA, SLStrecke).getY() == iec.getIntersectionPoint(segmE, SLStrecke).getY()) { //f�gt SegmE an Stelle mit richtiger absteigender Y Reihenfolge an x Koordinate des linken punkts von SegmE  
					SL.add(i+1,segmE);
					return new Strecke[] {SegmA,SegmB};
				}
			}
			//f�gt SegmE ganz hinten ein
			SL.add(segmE);
			return new Strecke[]{SegmB,null};
	}

	public static boolean insertIntersection(Point2D.Float intersectionPoint, Strecke segmA, Strecke segmB) {

		if(intersectionPoint != null ) {		//Wenn sie sich schneiden oder IntersectionPoint schon in L
			Event e = new Event(segmA, segmB) {
				public void doEvent() {
					treatIntersection(intersectionPoint,this);
				}
			};
			SimpleEntry<Point2D.Float,Event> se = new SimpleEntry<Point2D.Float,Event>(intersectionPoint, e);
			if(L.contains(se))
				return false;
			
			Event queueEntry = intersectionExists(intersectionPoint);

			//Wenns Intersection noch nicht gibt 
			if(queueEntry == null  ||
					!((queueEntry.strecke.equals(segmA) && queueEntry.strecke2.equals(segmB)) || (queueEntry.strecke.equals(segmB) && queueEntry.strecke2.equals(segmA)))) {   //Wenn das Intersection Event noch nicht in der EventQueue ist
				eventQueue.put((float) intersectionPoint.getX(), new Event(segmA, segmB) {
					public void doEvent() {
						treatIntersection(intersectionPoint,this);
					}
				});
				return true;
			}	
		}
		return false;
	}

	//Methode die pr�ft ob es den Intersectionpoint schon gibt,wenn nicht wird null zur�ckgegeben
	public static Event intersectionExists(Point2D.Float intersectionPoint) {
		//Hier d�rfen nur  Intersectionpoints gepr�ft werden
		float distCeiling = 10, distFloor = 10;
		if(eventQueue.ceilingEntry((float) intersectionPoint.getX()) != null)
			distCeiling = Math.abs(eventQueue.ceilingEntry((float) intersectionPoint.getX()).getKey() - (float) intersectionPoint.getX());
		if(eventQueue.floorEntry((float) intersectionPoint.getX()) != null)
			distFloor = Math.abs((float) intersectionPoint.getX() - eventQueue.floorEntry((float) intersectionPoint.getX()).getKey());
		
		if(distCeiling <= distFloor && distCeiling < 0.000001) {
			if(eventQueue.floorEntry((float) intersectionPoint.getX()) != null) {
				Event e = (Event) eventQueue.ceilingEntry((float) intersectionPoint.getX()).getValue();
				if(e.strecke != null && e.strecke2 != null)
					return (Event) e;
				else
					return null;
			}
		}
		
		if(distCeiling >= distFloor && distFloor < 0.000001) {
			if(eventQueue.floorEntry((float) intersectionPoint.getX()) != null) {
				Event e = (Event) eventQueue.floorEntry((float) intersectionPoint.getX()).getValue();
				if(e.strecke != null && e.strecke2 != null)
					return e;
				else
					return null;
			}

		}
		return null;
	}
	
	public static void treatRightEndpoint(SimpleEntry<Float,Strecke> e) {
		Strecke segmE = (Strecke) e.getValue();
		int index = findStreckeInSweepLine(segmE);
		//Wenn segmE nicht h�chste oder niedrigste Strecke in SL ist und nicht -1
		if(index != -1 && index != 0 && index != SL.size()-1) {
			Strecke segmA = SL.get(index - 1);
			Strecke segmB = SL.get(index + 1);
			Point2D.Float intersectionPoint = iec.getIntersectionPoint(segmA, segmB);
			insertIntersection(intersectionPoint, segmA,segmB);
		}		
		if(index != -1)
			SL.remove(index);
	}
	
	public static int findStreckeInSweepLine(Strecke s) {
		for(int i = 0; i < SL.size(); i++) {
			if(s.equals(SL.get(i)))
				return i;
		}
		return -1;
	}
	
	public static void treatIntersection(Point2D.Float intersectionPoint, Event e) {
		Strecke segmA = null, segmB = null;
		//Zu Ausgabeliste hinzuf�gen
		L.add(new SimpleEntry<Point2D.Float,Event>(intersectionPoint, e));
		
		//Schneidende Linien in SL tauschen
		int indexStrecke1 = findStreckeInSweepLine(e.strecke);
		int indexStrecke2 = findStreckeInSweepLine(e.strecke2);
		Strecke swap = SL.get(indexStrecke1);
		SL.set(indexStrecke1,SL.get(indexStrecke2));
		SL.set(indexStrecke2,swap);
		
		//SegmA und SegmB in SL finden und mit Strecke 1 und Strecke 2 auf Schnittpunkt testen
		if(indexStrecke1 < indexStrecke2) {  //Strecke 1 war vorher h�her als Strecke 2
			if(indexStrecke1 != 0) { //Falls es nicht schon die h�chste Strecke war
				segmA = SL.get(indexStrecke1 -1);	//SegmentA ist 1 h�her als Strecke 1 die vor Tausch h�her als Strecke 2 war
				final Point2D.Float iP = iec.getIntersectionPoint(e.strecke2, segmA);
				insertIntersection(iP,e.strecke2,segmA);
			}
			if(indexStrecke2 < SL.size()-2) {//Falls es nicht schon die unterste Strecke war
				segmB = SL.get(indexStrecke2 +1);	//SegmentB ist 1 niedriger als Strecke 2 die Tausch niedriger als Strecke 1 war
				final Point2D.Float iP2 = iec.getIntersectionPoint(e.strecke, segmB);
				insertIntersection(iP2,e.strecke,segmB);
			}
		}
		if(indexStrecke1 > indexStrecke2) {   //Strecke 1 war vorher niedriger als Strecke 2

			if(indexStrecke2 != 0) {
				segmA = SL.get(indexStrecke2 - 1);//SegmentA ist 1 h�her als Strecke 2 die vor Tausch h�her als Strecke 1 war
				final Point2D.Float iP2 = iec.getIntersectionPoint(e.strecke, segmA);
				insertIntersection(iP2,e.strecke,segmA);
			}
			if(indexStrecke2 < SL.size()-2) {
				segmB = SL.get(indexStrecke1 + 1);//SegmentB ist 1 niedriger als Strecke 1 die vor Tausch niedriger als Strecke 2 war
				final Point2D.Float iP = iec.getIntersectionPoint(e.strecke2, segmB);
				insertIntersection(iP,e.strecke2,segmB);
			}
		}
		int t = 0;
	}
	public static void doOutput() {
		System.out.println("Es exisitieren " + L.size() + "Schnittpunkte");
	}
}
