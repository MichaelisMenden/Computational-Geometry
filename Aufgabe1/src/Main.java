import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



public class Main {

	private static ArrayList<Strecke> Strecken = new ArrayList<Strecke>();
	
	public static void main(String[] args) {
		readFile();

	}
	
	private static void readFile() {
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

	}
	
	private int ccw(float p1, float p2, float p3) {
		
		return 0;
	}

}
