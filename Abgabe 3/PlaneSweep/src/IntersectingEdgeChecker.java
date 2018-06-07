import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Klasse welche Methoden zur Verfügung stellt um zu prüfen ob Strecken sich schneiden.
 * @Author: Patrick Burger, Michael Wimmer 
 */

public class IntersectingEdgeChecker {
	
	/*
	 * Methode zum Einlesen von Strecken aus Datei
	 * @param path Pfad zur Datei mit Strecken
	 * @return 	   Liste mit allen in Datei vorhandenen Strecken
	 */
	protected ArrayList<Strecke> readFile(String path) {
		ArrayList<Strecke> Strecken = new ArrayList<Strecke>();
		FileReader fr;
		try {
			fr = new FileReader(path);
		    BufferedReader br = new BufferedReader(fr);

		    String zeile = br.readLine();
		    while(zeile != null) {
		    	String[] zeilenArray = zeile.split(" ");
		    	//Die die links anfangen sind die Startpunkte
		    	double x1 = Double.parseDouble(zeilenArray[0]);
		    	double x2 = Double.parseDouble(zeilenArray[2]);
		    	Strecke s;
		    	if(x1 <= x2) {
		    		s = new Strecke(x1,Double.parseDouble(zeilenArray[1]),x2,Double.parseDouble(zeilenArray[3]));
		    	}
		    	else {
		    		s = new Strecke(x2,Double.parseDouble(zeilenArray[3]),x1,Double.parseDouble(zeilenArray[1]));
		    	}	    	
		    	Strecken.add(s);
		    	zeile = br.readLine();
		    }
		    br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Strecken;
	}
	
	/*
	 * Methode die prüft ob Punkt gegen den Uhrzeigersinn angeordnet sind.
	 * @param p1 Erster Punkt
	 * @param p2 Zweiter Punkt
	 * @param p3 Dritter Punkt
	 * @return   +: Im Uhrzeigersinn -: Gegen den Uhrzeigersinn
	 */
	public double ccw(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
		double edge[] = new double[3];
		edge[0] = (p2.x - p1.x) * (p2.y + p1.y);
		edge[1] = (p3.x - p2.x) * (p3.y + p2.y);
		edge[2] = (p1.x - p3.x) * (p1.y + p3.y);
		return edge[0] + edge[1] + edge[2];
	}
	
	/*
	 * Methode die prüft ob zwei Strecken sich schneiden
	 * @param s1 Erste Strecke
	 * @param s2 Zweite Strecke
	 * return 	 0: Kolinear und überlappend, 1: Schneiden sich -1: Schneiden sich nicht
	 */
	public int doIntersect(Strecke s1, Strecke s2) {
		//kolinear und überlapppend
			double test = ccw(s1.getStartPoint(), s1.getEndPoint(),s2.getStartPoint());
			double test1 = ccw(s1.getStartPoint(), s1.getEndPoint(),s2.getEndPoint());
		if (ccw(s1.getStartPoint(), s1.getEndPoint(),s2.getStartPoint()) == 0 && ccw(s1.getStartPoint(), s1.getEndPoint(),s2.getEndPoint()) == 0) {
			return 0;	
		}
		//Start und Endpunkt von s1 liegen auf verschiedenen Seiten von s2 und umgekehrt
		if (ccw(s1.getStartPoint(),s1.getEndPoint(),s2.getStartPoint()) * ccw(s1.getStartPoint(),s1.getEndPoint(),s2.getEndPoint()) <= 0 &&
			ccw(s2.getStartPoint(),s2.getEndPoint(),s1.getStartPoint()) * ccw(s2.getStartPoint(),s2.getEndPoint(),s1.getEndPoint()) <= 0) {
			return 1;
		}
		//Schneiden sich nicht
		return -1;
	}
	
	/*
	 * Methode die  den Schnittpunkt zwischen zwei Strecken berechnet.
	 * @param s1 Erste Strecke
	 * @param s2 Zweite Strecke
	 * @return Schnittpunkt
	 */
	public Point2D.Double getIntersectionPoint(Strecke s1, Strecke s2) {
		if(doIntersect(s1,s2) == 1) {
			Point2D.Double p1 = s1.getStartPoint();
			Point2D.Double p2 = s1.getEndPoint();
			Point2D.Double p3 = s2.getStartPoint();
			Point2D.Double p4 = s2.getEndPoint();
			double xs = (double) (((p4.getX() - p3.getX()) * (p2.getX()*p1.getY() - p1.getX()*p2.getY()) - (p2.getX() - p1.getX()) * (p4.getX()*p3.getY() - p3.getX()*p4.getY())) / ((p4.getY() - p3.getY()) * (p2.getX() - p1.getX()) - (p2.getY() - p1.getY()) * (p4.getX() - p3.getX())));
			double ys = (double) (((p1.getY() - p2.getY()) * (p4.getX()*p3.getY() - p3.getX()*p4.getY()) - (p3.getY() - p4.getY()) * (p2.getX()*p1.getY() - p1.getX()*p2.getY())) / ((p4.getY() - p3.getY()) * (p2.getX() - p1.getX()) - (p2.getY() - p1.getY()) * (p4.getX() - p3.getX())));
			return new Point2D.Double(xs, ys);
		}
		//Falls kolinear überlappend(z.B. bei Linie die nur Punkt ist)
		if(doIntersect(s1,s2) == 0) {
			if(s1.getStartPoint().equals(s1.getEndPoint())) {
				return s1.getStartPoint();
			}
			if(s2.getStartPoint().equals(s2.getEndPoint())) {
				return s2.getStartPoint();
			}
		}
		
		return null;
	}

}
