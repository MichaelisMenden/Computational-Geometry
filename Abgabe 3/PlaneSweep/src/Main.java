import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
/*
 * Main Klasse des SweepLine Algorithmus. Enthält die Main Methode sowie die wichtisten Methoden die für den Algorithmus
 * nötig sind.
 * @Author: Patrick Burger, Michael Wimmer
 */

public class Main {

	private static TreeMap<Double, Event> eventQueue = new TreeMap<Double, Event>();
	private static ArrayList<Strecke> SL = new ArrayList<Strecke>();
	private static ArrayList<SimpleEntry<Point2D.Double, Event>> L = new ArrayList<SimpleEntry<Point2D.Double, Event>>();
	static ArrayList<Strecke> Strecken;
	private static IntersectingEdgeChecker iec = new IntersectingEdgeChecker();
	private static Strecke SLStrecke = new Strecke(0, -10000, 0, 10000);
	private static ArrayList<Strecke> specialLines = new ArrayList<Strecke>();
	static int count = 1;

	/*
	 * Main Methode in der alle Streckendateien eingelesen werden und mit LineSweep untersucht werden
	 */
	public static void main(String[] args) throws IOException {
		LineSweep(".\\src\\s_1000_1.dat");
		clearLists();
		LineSweep(".\\src\\s_1000_10.dat");
		clearLists();
		LineSweep(".\\src\\s_10000_1.dat");
		clearLists();
		LineSweep(".\\src\\s_100000_1.dat");
		clearLists();
	}
	
	/*
	 * LineSweep Methode in der alle Punkte in die EventQueue geladen werden und anschließend in aufsteigender Reihenfolge nach 
	 * ihrer x-Koordinate abgearbeitet werden bis diese leer ist. Spezielle Linien wie z.B. senkrechte werden gesondert behandelt.
	 */
	public static void LineSweep(String path) throws IOException {
		initEventQueue(path);
		
		long start = System.currentTimeMillis();
		while (!eventQueue.isEmpty()) {
			Entry<Double, Event> E = eventQueue.firstEntry();
			double x = E.getKey();

			E.getValue().doEvent();
			eventQueue.remove(E.getKey());
			//System.out.println(eventQueue.size());
			// System.out.println(L.size());
		}
		treatSpecialLines();
		long end = System.currentTimeMillis();
		doOutput(path, end - start);
		
	}
	/*
	 * Methode zum Initialisieren der eventQueue. Spezielle Linien werden in die Liste specialLines gespeichert.
	 */
	public static void initEventQueue(String path) {
		Strecken = iec.readFile(path);
		for (int i = 0; i < Strecken.size(); i++) {
			Strecke strecke = Strecken.get(i);
			// Behandlung von vertikalen Strecken
			if (strecke.getxStart() == strecke.getxEnd() && strecke.getyStart() != strecke.getyEnd()) {
				specialLines.add(strecke);
				continue;
			}
			// Behandlung von Strecke bei der Anfangs und Endpunkt gleich sind
			if (strecke.getStartPoint().equals(strecke.getEndPoint())) {
				specialLines.add(strecke);
				continue;
			}
			double xLeft = strecke.getxStart();
			double xRight = strecke.getxEnd();
			char a = 'a';
			double[] keys = keyCollision(xLeft, xRight);
			eventQueue.put(keys[0], new Event(strecke) {
				public void doEvent() {
					treatLeftEndpoint(new SimpleEntry<Double, Strecke>(keys[0],
							strecke));
				}
			});
			eventQueue.put(keys[1], new Event(strecke) {
				public void doEvent() {
					treatRightEndpoint(new SimpleEntry<Double, Strecke>(keys[1],
							strecke));
				}
			});
		}
	}

	/*
	 * Methode die darauf achtet dass kein key in die eventQueue geschrieben wird welcher schon vorhanden ist,
	 * da der alte Value sonst überschrieben würde. Falls der key schon vorhanden ist wird der Wert minimal erhöht oder verkleinert.
	 * @param xLeft Key des Startpunkts einer Linie
	 * @param xRight Key des Endpunkts einer Linie
	 * @return Array mit den ggf. modifizierten keys
	 */
	private static double[] keyCollision(double xLeft, double xRight) {
		while (eventQueue.containsKey(xLeft))
			xLeft += .0000001d;
		while (eventQueue.containsKey(xRight))
			xRight -= .0000001d;
		return new double[] { xLeft, xRight };

	}

	/*
	 * Behandlungsroutine für linken Startpunkt einer Linie. Die Strecke wird zur SweelLine hinzugefügt und es wird 
	 * geprüft ob sie sich mit den sie umgebenden Strecken schneidet. Falls dies der Fall ist wird der Schnittpunkt in die
	 * EventQueue eingefügt.
	 * @param Eintrag mit Key der EventQueue sowie der zugehörigen Strecke
	 */
	public static void treatLeftEndpoint(SimpleEntry<Double, Strecke> e) {
		Strecke[] s = addToSL(e.getValue());

		if (s[0] != null) {
			final Point2D.Double intersectionPoint1 = iec.getIntersectionPoint((Strecke) e.getValue(), s[0]);
			insertIntersection(intersectionPoint1, (Strecke) e.getValue(), s[0]);
		}
		if (s[1] != null) {
			final Point2D.Double intersectionPoint2 = iec.getIntersectionPoint((Strecke) e.getValue(), s[1]);
			insertIntersection(intersectionPoint2, (Strecke) e.getValue(), s[1]);
		}
	}

	/*
	 * Methode die eine Strecke in die Beobachtung der Sweepline einfügt. Es wird der richtige Platz für die Strecke
	 * entsprechend ihres absteigenden Y-Werts gesucht.
	 * @param Strecke die zur SweepLine hinzugefügt werden soll
	 * @return Array mit Strecken die sich über und unter der eingefügten Strecke befinden
	 */
	private static Strecke[] addToSL(Strecke segmE) {
		Strecke SegmA = null, SegmB = null;
		SLStrecke.setBothX(segmE.getxStart());

		// Falls SL leer ist
		if (SL.size() == 0) { 
			SL.add(segmE);
			return new Strecke[] { null, null };
		}
		//Falls nur 1 Eintrag in SL
		if (SL.size() == 1) {
			SegmA = SL.get(0);
			Point2D.Double pA = iec.getIntersectionPoint(SegmA, SLStrecke);
			if(pA.getY() >= segmE.getxStart()) {
				SL.add(1, segmE);
				return new Strecke[] { SegmA, null };
			}
			else {
				SL.add(0,segmE);
				return new Strecke[] { null, SegmA };
			}
		}
		//Falls einzufügendes Segment höher als erstes Element in SweepLine
		SegmA = SL.get(0);
		if(iec.getIntersectionPoint(SegmA, SLStrecke).y < iec.getIntersectionPoint(segmE, SLStrecke).y) {
			SL.add(0,segmE);
			return new Strecke[] { null, SegmA };
		}

		// fügt SegmE an Stelle mit richtiger absteigender Y Reihenfolge, an X-Koordinate an der SL gerade steht, ein
		for (int i = 0; i < SL.size() - 1; i++) {
			SegmA = SL.get(i);
			SegmB = SL.get(i + 1);
			Point2D.Double pA = iec.getIntersectionPoint(SegmA, SLStrecke);
			Point2D.Double pB = iec.getIntersectionPoint(SegmB, SLStrecke);
			Point2D.Double pE = iec.getIntersectionPoint(segmE, SLStrecke);

			if ((pA.getY() >= pE.getY() && pB.getY() < pE.getY())
					|| pA.getY() == pE.getY()) { 
				SL.add(i + 1, segmE);
				return new Strecke[] { SegmA, SegmB };
			}
		}
		// fügt SegmE ganz hinten ein
		SL.add(segmE);
		return new Strecke[] { SegmB, null };
	}

	/*
	 * Fügt einen neuen Schnittpunkt von Strecken in die Output-Liste L ein
	 * @param intersectionPoint Schnittpunkt der Strecken
	 * @param segmA 		    Erste Strecke von Schnittpunkt
	 * @param segmB 			Zweite Strecke von Schnittpunkt
	 * @return 					true falls Schnittpunkt eingefügt wurde, false falls Schnittpunkt nicht eingefügt wurde
	 */
	public static boolean insertIntersection(Point2D.Double intersectionPoint, Strecke segmA, Strecke segmB) {

		if (intersectionPoint != null) { 
			Event e = new Event(segmA, segmB) {
				public void doEvent() {
					treatIntersection(intersectionPoint, this);
				}
			};
			SimpleEntry<Point2D.Double, Event> se = new SimpleEntry<Point2D.Double, Event>(
					intersectionPoint, e);
			// Falls Schittpunkt Anfangspunkt eines der Segmente oder IntersectionPoint schon in L
			if ((segmA.getStartPoint().equals(intersectionPoint) | segmB.getStartPoint().equals(intersectionPoint)) &&
					!L.contains(se)) {
				L.add(se);
				return true;
			}

			if (L.contains(se))
				return false;

			Event queueEntry = intersectionExists(intersectionPoint);

			// Wenns Intersection noch nicht gibt
			if (queueEntry == null
					// Wenn das Intersection Event noch nicht in der EventQueue ist
					|| !((queueEntry.strecke.equals(segmA) && queueEntry.strecke2.equals(segmB)) || (queueEntry.strecke.equals(segmB) && queueEntry.strecke2.equals(segmA)))) { 

				// Falls Anfangs oder Endpunkt in eventQueue mit gleichem Key
				while (eventQueue.containsKey((double) intersectionPoint.getX()))
					intersectionPoint.x = (double) intersectionPoint.getX() + 0.0001d;

				eventQueue.put((double) intersectionPoint.getX(), new Event(
						segmA, segmB) {
					public void doEvent() {
						treatIntersection(intersectionPoint, this);
					}
				});
				return true;
			}
		}
		return false;
	}

	/*
	 *  Methode die prüft ob es den Intersectionpoint schon gibt.
	 *  @param intersectionPoint Schnittpunkt der geprüft werden soll
	 *  @return Event falls Schnittpunkt bereits exisitiert, sonst null
	 */
	
	public static Event intersectionExists(Point2D.Double intersectionPoint) {
		// Hier dürfen nur Intersectionpoints geprüft werden
		double distCeiling = 10, distFloor = 10;
		if (eventQueue.ceilingEntry((double) intersectionPoint.getX()) != null)
			distCeiling = Math.abs(eventQueue.ceilingEntry((double) intersectionPoint.getX()).getKey() - (double) intersectionPoint.getX());
		if (eventQueue.floorEntry((double) intersectionPoint.getX()) != null)
			distFloor = Math.abs((double) intersectionPoint.getX()- eventQueue.floorEntry((double) intersectionPoint.getX()).getKey());

		if (distCeiling <= distFloor && distCeiling < 0.000001) {
			if (eventQueue.floorEntry((double) intersectionPoint.getX()) != null) {
				Event e = (Event) eventQueue.ceilingEntry((double) intersectionPoint.getX()).getValue();
				if (e.strecke != null && e.strecke2 != null)
					return (Event) e;
				else
					return null;
			}
		}

		if (distCeiling >= distFloor && distFloor < 0.000001) {
			if (eventQueue.floorEntry((double) intersectionPoint.getX()) != null) {
				Event e = (Event) eventQueue.floorEntry((double) intersectionPoint.getX()).getValue();
				if (e.strecke != null && e.strecke2 != null)
					return e;
				else
					return null;
			}

		}
		return null;
	}
	/*
	 * Behandlungsroutine für rechten Endpunkt einer Strecke.
	 * @param Eintrag mit Key der EventQueue sowie der zugehörigen Strecke
	 */
	public static void treatRightEndpoint(SimpleEntry<Double, Strecke> e) {
		Strecke segmE = (Strecke) e.getValue();
		int index = findStreckeInSweepLine(segmE);
		// Wenn segmE nicht höchste oder niedrigste Strecke in SL ist und nicht
		// -1
		if (index != -1 && index != 0 && index != SL.size() - 1) {
			Strecke segmA = SL.get(index - 1);
			Strecke segmB = SL.get(index + 1);
			Point2D.Double intersectionPoint = iec.getIntersectionPoint(segmA,
					segmB);
			// Falls durch Numerik ein Schnittpunkt weiter rechts gesetzt wird
			// als Endpunkt
			if (intersectionPoint != null && Math.abs(intersectionPoint.x - segmA.getxEnd()) < 0.00001
					&& intersectionPoint.x > segmA.getxEnd()) {
				intersectionPoint.x = segmA.getxEnd() - 0.00001d;
			}
			if (intersectionPoint != null && Math.abs(intersectionPoint.x - segmB.getxEnd()) < 0.00001
					&& intersectionPoint.x > segmB.getxEnd()) {
				intersectionPoint.x = segmB.getxEnd() - 0.00001d;
			}
			insertIntersection(intersectionPoint, segmA, segmB);
		}
		if (index != -1)
			SL.remove(index);
	}

	/*
	 * Methode die eine Strecke in der SweepLine findet.
	 * @param  s Strecke die gesucht wird
	 * @return index in SweepLine
	 */
	public static int findStreckeInSweepLine(Strecke s) {
		for (int i = 0; i < SL.size(); i++) {
			if (s.equals(SL.get(i)))
				return i;
		}
		return -1;
	}

	/*
	 * Methode die einen Schnittpunkt aus der EventQueue behandelt. Die beteiligten Strecken werden in SweepLine getauscht
	 * und mit den getauschten Strecken wird geprüft ob diese sich jeweils mit ihren oberen oder unteren Nachbarn schneiden.
	 * @param intersectionPoint Schnittpunkt
	 * @param e					IntersectionEvent welches beide beteiligten Strecken enthält
	 */
	public static void treatIntersection(Point2D.Double intersectionPoint, Event e) {
		Strecke segmA = null, segmB = null;
		// Zu Ausgabeliste hinzufügen
		L.add(new SimpleEntry<Point2D.Double, Event>(intersectionPoint, e));

		// Schneidende Linien in SL tauschen
		int indexStrecke1 = findStreckeInSweepLine(e.strecke);
		int indexStrecke2 = findStreckeInSweepLine(e.strecke2);
		Strecke swap = SL.get(indexStrecke1);
		SL.set(indexStrecke1, SL.get(indexStrecke2));
		SL.set(indexStrecke2, swap);

		// SegmA und SegmB in SL finden und mit Strecke 1 und Strecke 2 auf
		// Schnittpunkt testen
		if (indexStrecke1 < indexStrecke2) { // Strecke 1 war vorher höher als Strecke 2
			if (indexStrecke1 != 0) { // Falls es nicht schon die höchste Strecke war
				segmA = SL.get(indexStrecke1 - 1); // SegmentA ist 1 höher als Strecke 1 die vor Tausch höher als Strecke 2 war
				final Point2D.Double iP = iec.getIntersectionPoint(e.strecke2,segmA);
				insertIntersection(iP, e.strecke2, segmA);
			}
			if (indexStrecke2 < SL.size() - 1) {// Falls es nicht schon die unterste Strecke war
				segmB = SL.get(indexStrecke2 + 1); // SegmentB ist 1 niedriger als Strecke 2 die Tausch niedriger als Strecke 1 war
				final Point2D.Double iP2 = iec.getIntersectionPoint(e.strecke, segmB);
				insertIntersection(iP2, e.strecke, segmB);
			}
		}
		if (indexStrecke1 > indexStrecke2) { // Strecke 1 war vorher niedriger als Strecke 2
			if (indexStrecke2 != 0) {		// Falls es nicht schon die höchste Strecke war
				segmA = SL.get(indexStrecke2 - 1);// SegmentA ist 1 höher als Strecke 2 die vor Tausch höher als Strecke 1 war
				final Point2D.Double iP2 = iec.getIntersectionPoint(e.strecke, segmA);
				insertIntersection(iP2, e.strecke, segmA);
			}
			if (indexStrecke1 < SL.size() - 1) {
				segmB = SL.get(indexStrecke1 + 1);// SegmentB ist 1 niedriger als Strecke 1 die vor Tausch niedriger als Strecke 2 war
				final Point2D.Double iP = iec.getIntersectionPoint(e.strecke2, segmB);
				insertIntersection(iP, e.strecke2, segmB);
			}
		}
	}

	/*
	 * Ausgabe Methode am Ende des Algorithmus die alle Schnittpunkte mit zugehörigen Strecken ausgibt sowie die 
	 * insgesamte Anzahl der Schnittpunkte ausgibt.
	 */
	public static void doOutput(String path, long duration) throws IOException {
		try{
			System.out.println("Folgende Linien schneiden sich in Datei " + path + " : ");
			FileWriter fw = new FileWriter("ausgabe"+ count++ +".txt");
		    BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < L.size(); i++) {
				Strecke s1 = L.get(i).getValue().strecke;
				Strecke s2 = L.get(i).getValue().strecke2;
				Point2D.Double p = L.get(i).getKey();
				System.out.println(s1 + " " + s2 + " " + p.getX() + " " + p.getY());
			    bw.write(s1 + " " + s2 + "\r\n");
			}
		    
			System.out.println("Es exisitieren " + L.size() + "Schnittpunkte");
			System.out.println(duration + " ms");
			bw.write("Es exisitieren " + L.size() + "Schnittpunkte");
			bw.write(duration + " ms");
			bw.close();
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}

	/*
	 * Methode die spezielle Strecken wie vertikale oder Strecken bei denen Start- und Endpunkt gleich sind nach dem eigentlichen
	 * Algorithmus testet.
	 */
	public static void treatSpecialLines() {
		// Behandlung spezieller Linien
		for (Strecke specialLine : specialLines) {
			for (Strecke strecke : Strecken) {
				// Behandlung von Strecken deren Anfangs und Endpunkt gleich ist
				if (specialLine.getStartPoint().equals(specialLine.getEndPoint())) {
					// doIntersect kann nicht als einziges Kriterium verwendet
					// werden, daher zusätzliche Abfrage ob überschneidung möglich ist
					if (iec.doIntersect(specialLine, strecke) == 0 && ((specialLine.getxStart() >= strecke.getxStart() && 
						specialLine.getxStart() <= strecke.getxEnd()) && (specialLine.getyStart() >= strecke.getyStart() &&
						specialLine.getyStart() <= strecke.getyEnd()))&& !specialLine.equals(strecke)) {
						Point2D.Double intersectionPoint = iec.getIntersectionPoint(specialLine, strecke);
						Event e = new Event(specialLine, strecke) {
							public void doEvent() {
								treatIntersection(intersectionPoint, this);
							}
						};
						SimpleEntry<Point2D.Double, Event> se = new SimpleEntry<Point2D.Double, Event>(
								intersectionPoint, e);
						if(!L.contains(se))
							L.add(se);
						continue;
					}
				} else {
					// Behandlung vertikaler Strecken
					if (iec.getIntersectionPoint(specialLine, strecke) != null) {
						Point2D.Double intersectionPoint = iec.getIntersectionPoint(specialLine, strecke);
						Event e = new Event(specialLine, strecke) {
							public void doEvent() {
								treatIntersection(intersectionPoint, this);
							}
						};
						SimpleEntry<Point2D.Double, Event> se = new SimpleEntry<Point2D.Double, Event>(intersectionPoint, e);
						if(!L.contains(se))
							L.add(se);
						continue;
					}
				}
			}
		}
	}
	
	public static void clearLists() {
		L.clear();
		eventQueue.clear();
		SL.clear();
		Strecken.clear();
		SLStrecke = new Strecke(0, -10000, 0, 10000);
		specialLines.clear();
	}
}
