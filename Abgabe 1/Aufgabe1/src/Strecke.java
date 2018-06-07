import java.awt.geom.Point2D;

/*
 * Klasse die eine Strecke mit Startpunkt und Endpunkt repr�sentiert.
 * @Author: Patrick Burger, Michael Wimmer
 */

public class Strecke {
	private Point2D.Float start = new Point2D.Float();
	private Point2D.Float end = new Point2D.Float();
	
	/*
	 * Konstruktor zum Erstellen eines Streckenobjekts mit jeweils x und y Komponente f�r Start und Endpunkt.
	 * @param xStart X-Koordinate des Startpunkts
	 * @param yStart Y-Koordinate des Startpunkts
	 * @param xEnd 	 X-Koordinate des Endpunkts
	 * @param yEnd   Y-Koordinate des Endpunkts
	 */
	public Strecke(float xStart, float yStart, float xEnd, float yEnd) {
		start.x = xStart;
		start.y = yStart;
		end.x = xEnd;
		end.y = yEnd;
	}
	/*
	 * Getter f�r Startpunkt
	 * @return Startpunkt
	 */
	public Point2D.Float getStartPoint() {
		return start;
	}
	/*
	 * Getter f�r Endpunkt
	 * @return Endpunkt
	 */
	public Point2D.Float getEndPoint() {
		return end;
	}
	/*
	 * Getter f�r X-Koordinate des Startpunkts
	 * @return X-Koordinate des Startpunkts
	 */
	public float getxStart() {
		return start.x;
	}
	/*
	 * Getter f�r Y-Koordinate des Startpunkts
	 * @return Y-Koordinate des Startpunkts
	 */
	public float getyStart() {
		return start.y;
	}

	/*
	 * Getter f�r X-Koordinate des Endpunkts
	 * @return X-Koordinate des Endpunkts
	 */
	public float getxEnd() {
		return end.x;
	}

	/*
	 * Getter f�r Y-Koordinate des Endpunkts
	 * @return Y-Koordinate des Endpunkts
	 */
	public float getyEnd() {
		return end.y;
	}
}
