import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class IntersectingEdgeChecker {
	
	
	
	protected ArrayList<Strecke> readFile() {
		ArrayList<Strecke> Strecken = new ArrayList<Strecke>();
		FileReader fr;
		try {
			fr = new FileReader("C:\\Users\\Michi\\Dropbox\\Michi Uniordner\\Master\\2.Semester\\Computational Geometry\\Aufgabe1\\src\\s_1000_1.dat");
		    BufferedReader br = new BufferedReader(fr);

		    String zeile = br.readLine();
		    while(zeile != null) {
		    	String[] zeilenArray = zeile.split(" ");
		    	Strecke s = new Strecke(Float.parseFloat(zeilenArray[0]),Float.parseFloat(zeilenArray[1]),Float.parseFloat(zeilenArray[2]),Float.parseFloat(zeilenArray[3]));
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
		float edge[] = new float[3];  //Funktioniert das auch mit Float? ich hoffe ja :D
		edge[0] = (p2.x - p1.x) * (p2.y + p1.y);
		edge[1] = (p3.x - p2.x) * (p3.y + p2.y);
		edge[2] = (p1.x - p3.x) * (p1.y + p3.y);
		return edge[0] + edge[1] + edge[2];
	}

}
