import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class IntersectingEdgeChecker {
	
	
	
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
		    	float x1 = Float.parseFloat(zeilenArray[0]);
		    	float x2 = Float.parseFloat(zeilenArray[2]);
		    	Strecke s;
		    	if(x1 <= x2) {
		    		s = new Strecke(x1,Float.parseFloat(zeilenArray[1]),x2,Float.parseFloat(zeilenArray[3]));
		    	}
		    	else {
		    		s = new Strecke(x2,Float.parseFloat(zeilenArray[3]),x1,Float.parseFloat(zeilenArray[1]));
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
	
	public float ccw(Point2D.Float p1, Point2D.Float p2, Point2D.Float p3) {
		float edge[] = new float[3];
		edge[0] = (p2.x - p1.x) * (p2.y + p1.y);
		edge[1] = (p3.x - p2.x) * (p3.y + p2.y);
		edge[2] = (p1.x - p3.x) * (p1.y + p3.y);
		return edge[0] + edge[1] + edge[2];
	}
	
	public int doIntersect(Strecke s1, Strecke s2) {
		//kolinear und überlapppend
			float test = ccw(s1.getStartPoint(), s1.getEndPoint(),s2.getStartPoint());
			float test1 = ccw(s1.getStartPoint(), s1.getEndPoint(),s2.getEndPoint());
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
	
	public Point2D.Float getIntersectionPoint(Strecke s1, Strecke s2) {
		//Tausch falls s1 Linie mit nur einem Punkt ist
		/*if(s1.getStartPoint().equals(s1.getEndPoint())) {
			Strecke swap = s1;
			s1 = s2;
			s2 = swap;
		}*/
		int i = doIntersect(s1,s2);
		if(doIntersect(s1,s2) == 1) {
			Point2D.Float p1 = s1.getStartPoint();
			Point2D.Float p2 = s1.getEndPoint();
			Point2D.Float p3 = s2.getStartPoint();
			Point2D.Float p4 = s2.getEndPoint();
			float xs = (float) (((p4.getX() - p3.getX()) * (p2.getX()*p1.getY() - p1.getX()*p2.getY()) - (p2.getX() - p1.getX()) * (p4.getX()*p3.getY() - p3.getX()*p4.getY())) / ((p4.getY() - p3.getY()) * (p2.getX() - p1.getX()) - (p2.getY() - p1.getY()) * (p4.getX() - p3.getX())));
			float ys = (float) (((p1.getY() - p2.getY()) * (p4.getX()*p3.getY() - p3.getX()*p4.getY()) - (p3.getY() - p4.getY()) * (p2.getX()*p1.getY() - p1.getX()*p2.getY())) / ((p4.getY() - p3.getY()) * (p2.getX() - p1.getX()) - (p2.getY() - p1.getY()) * (p4.getX() - p3.getX())));
			return new Point2D.Float(xs, ys);
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
