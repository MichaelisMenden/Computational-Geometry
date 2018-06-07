import java.util.ArrayList;

/*Main Klasse für Aufgabe 1.
 * Liest alle 3 Testdatensätze ein und gibt die Anzahl der Schnittpunkte sowie die benötgte Zeit aus
 * @Author Patrick Burger, Michael Wimmer
 */

public class Main {

	private static ArrayList<Strecke> Strecken = new ArrayList<Strecke>();
	private static int intersectingLines = 0;
	private static int overlappingLines = 0;
	
	public static void main(String[] args) {
		IntersectingEdgeChecker iec = new IntersectingEdgeChecker();
		//1.Datei
		String path = ".\\src\\s_1000_1.dat";
		Strecken = iec.readFile(path);
		long start = System.currentTimeMillis();
		for(Strecke s1 : Strecken) {
			for(Strecke s2 : Strecken) {
				if(s1.equals(s2))
					break;
				if(iec.doIntersect(s1, s2) == 1) {
					intersectingLines++;
				}
				if(iec.doIntersect(s1, s2) == 0) {
					overlappingLines++;
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("Die Datei '" + path + "' hat " + intersectingLines + " sich schneidende Strecken und " + overlappingLines + " sich überlappende Linien");
		System.out.println("Es wurden " + (end - start) + "Millisekunden gebraucht");
		
		//Streckenliste leeren
		Strecken.clear();
		
		//2.Datei
		path = ".\\src\\s_10000_1.dat";
		Strecken = iec.readFile(path);
		start = System.currentTimeMillis();
		for(Strecke s1 : Strecken) {
			for(Strecke s2 : Strecken) {
				if(s1.equals(s2))
					break;
				if(iec.doIntersect(s1, s2) == 1) {
					intersectingLines++;
				}
				if(iec.doIntersect(s1, s2) == 0) {
					overlappingLines++;
				}
			}
		}
		end = System.currentTimeMillis();
		System.out.println("Die Datei '" + path + "' hat " + intersectingLines + " sich schneidende Strecken und " + overlappingLines + " sich überlappende Linien");
		System.out.println("Es wurden " + (end - start) + "Millisekunden gebraucht");
		
		//Streckenliste leeren
		Strecken.clear();
		
		//3.Datei
		path = ".\\src\\s_100000_1.dat";
		Strecken = iec.readFile(path);
		start = System.currentTimeMillis();
		for(Strecke s1 : Strecken) {
			for(Strecke s2 : Strecken) {
				if(s1.equals(s2))
					break;
				if(iec.doIntersect(s1, s2) == 1) {
					intersectingLines++;
				}
				if(iec.doIntersect(s1, s2) == 0) {
					overlappingLines++;
				}
			}
		}
		end = System.currentTimeMillis();
		System.out.println("Die Datei '" + path + "' hat " + intersectingLines + " sich schneidende Strecken und " + overlappingLines + " sich überlappende Linien");
		System.out.println("Es wurden " + (end - start) + "Millisekunden gebraucht");
		
		//Streckenliste leeren
		Strecken.clear();
	}
	

	


}
