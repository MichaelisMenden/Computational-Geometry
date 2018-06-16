import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Main {
	
	static ArrayList<String> Ausgabe1 = new ArrayList<String>();
	static ArrayList<String> Ausgabe3 = new ArrayList<String>();

	public static void main(String[] args) {
		Ausgabe1 = readFile(".\\src\\ausgabe1.txt");
		Ausgabe3 = readFile(".\\src\\ausgabe3.txt");
		ArrayList<String> missingLines = new ArrayList<String>();
		for(String line : Ausgabe1) {
			if(!Ausgabe3.contains(line)) {
				String lines[] = line.split(" ");
				line = lines[1] + " " + lines[0];
				if(!Ausgabe3.contains(line)) {
					missingLines.add(line);
					System.out.println(line);
				}

			}
		}

		System.out.println(missingLines.size());
	}
	
	public static ArrayList<String> readFile(String path) {
		ArrayList<String> list = new ArrayList<String>();
		FileReader fr;
		try {
		fr = new FileReader(path);
		    BufferedReader br = new BufferedReader(fr);
		    String line = br.readLine();
		    
		    while(line != null) {
		    	list.add(line);
		    	line = br.readLine();
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;	
	}
	
	

}
