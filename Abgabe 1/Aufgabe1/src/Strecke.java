import java.awt.geom.Point2D;

/*
 * Klasse die eine Strecke mit Startpunkt und Endpunkt repräsentiert.
 * @Author: Patrick Burger, Michael Wimmer
 */

public class Strecke {
	private Point2D.Double start = new Point2D.Double();
	private Point2D.Double end = new Point2D.Double();

	/*
	 * Konstruktor zum Erstellen eines Streckenobjekts mit jeweils x und y
	 * Komponente für Start und Endpunkt.
	 * 
	 * @param xStart X-Koordinate des Startpunkts
	 * 
	 * @param yStart Y-Koordinate des Startpunkts
	 * 
	 * @param xEnd X-Koordinate des Endpunkts
	 * 
	 * @param yEnd Y-Koordinate des Endpunkts
	 */
	public Strecke(Double xStart, Double yStart, Double xEnd, Double yEnd) {
		start.x = xStart;
		start.y = yStart;
		end.x = xEnd;
		end.y = yEnd;
	}

	/*
	 * Getter für Startpunkt
	 * 
	 * @return Startpunkt
	 */
	public Point2D.Double getStartPoint() {
		return start;
	}

	/*
	 * Getter für Endpunkt
	 * 
	 * @return Endpunkt
	 */
	public Point2D.Double getEndPoint() {
		return end;
	}

	/*
	 * Getter für X-Koordinate des Startpunkts
	 * 
	 * @return X-Koordinate des Startpunkts
	 */
	public Double getxStart() {
		return start.x;
	}

	/*
	 * Getter für Y-Koordinate des Startpunkts
	 * 
	 * @return Y-Koordinate des Startpunkts
	 */
	public Double getyStart() {
		return start.y;
	}

	/*
	 * Getter für X-Koordinate des Endpunkts
	 * 
	 * @return X-Koordinate des Endpunkts
	 */
	public Double getxEnd() {
		return end.x;
	}

	/*
	 * Getter für Y-Koordinate des Endpunkts
	 * 
	 * @return Y-Koordinate des Endpunkts
	 */
	public Double getyEnd() {
		return end.y;
	}
	
	/*
	 * Methode die Strecken vergleicht
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		Strecke s = (Strecke) o;
		if(Math.abs(this.getxStart() - s.getxStart()) < 0.000000001 && Math.abs(this.getyStart() - s.getyStart()) < 0.000000001 && 
				Math.abs(this.getxEnd() - s.getxEnd()) < 0.000000001 && Math.abs(this.getyEnd() - s.getyEnd()) < 0.000000001) {
			return true;
		}
		return false;
	}

	/*
	 * Ausgabe einer Strecke (Formatiert zum einfügen in GeoGebra)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// return "Start-X= " + start.x + "  Start-Y= " + start.y + "  End-X= "
		// + end.x + "  End-Y= " + end.y;
		return "Strecke(Punkt[{" + start.x + "," + start.y + "}],Punkt[{"
				+ end.x + "," + end.y + "}])";
	}
}
