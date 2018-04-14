import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



public class Main {

	private static ArrayList<Strecke> Strecken = new ArrayList<Strecke>();
	
	public static void main(String[] args) {
		IntersectingEdgeChecker iec = new IntersectingEdgeChecker();
		Strecken = iec.readFile();
		
		Point2D.Float p1 = new Point2D.Float(0,0);
		Point2D.Float p2 = new Point2D.Float(1,1);
		Point2D.Float p3 = new Point2D.Float(0,1);
		System.out.println(iec.ccw(p1, p2, p3));

	}
	

	


}
