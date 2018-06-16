import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*Main Klasse für Aufgabe 1.
 * Liest alle 3 Testdatensätze ein und gibt die Anzahl der Schnittpunkte sowie die benötgte Zeit aus
 * @Author Patrick Burger, Michael Wimmer
 */

public class Main {

	private static ArrayList<Strecke> Strecken = new ArrayList<Strecke>();
	private static int intersectingLines = 0;
	private static int overlappingLines = 0;
	
	public static void main(String[] args) throws IOException {
		IntersectingEdgeChecker iec = new IntersectingEdgeChecker();
		//1.Datei
		String path = "s_1000_1.dat";
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
		intersectingLines = 0;
		overlappingLines = 0;
		
		//2.Datei
		path = "s_10000_1.dat";
		Strecken = iec.readFile(path);

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
		intersectingLines = 0;
		overlappingLines = 0;
		
		//3.Datei
		path = "s_100000_1.dat";
		Strecken = iec.readFile(path);
		FileWriter fw = new FileWriter("ausgabe.txt");
	    BufferedWriter bw = new BufferedWriter(fw);
		start = System.currentTimeMillis();
		start = System.currentTimeMillis();
		for(Strecke s1 : Strecken) {
			for(Strecke s2 : Strecken) {
				if(s1.equals(s2))
					break;
				if(iec.doIntersect(s1, s2) == 1) {
					intersectingLines++;
					bw.write(s1 + " " + s2 + "\r\n");
				}
				if(iec.doIntersect(s1, s2) == 0) {
					overlappingLines++;
					bw.write(s1 + " " + s2 + "\r\n");
				}
			}
		}
		end = System.currentTimeMillis();
		bw.close();
		System.out.println("Die Datei '" + path + "' hat " + intersectingLines + " sich schneidende Strecken und " + overlappingLines + " sich überlappende Linien");
		System.out.println("Es wurden " + (end - start) + "Millisekunden gebraucht");
		
		//Streckenliste leeren
		Strecken.clear();
	}
	

	


}
