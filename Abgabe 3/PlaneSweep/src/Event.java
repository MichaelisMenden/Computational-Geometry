
/*
 * Klasse die ein Event in der SweepLine darstellt. Es enthält zugehörige Strecken (Eine bei linem oder rechtem Endpunkt, zwei bei
 * Schnittpunkt) sowie eine Abstrakte Methode doEvent(). Sie wird je nach Art des Punktes in EventQueue zu treatLeftEndpoint,
 * treatRightEndpoint oder treatIntersectionPoint.
 * @Author: Patrick Burger, Michael Wimmer
 */
public abstract class Event {
	public Strecke strecke;
	public Strecke strecke2;
	
	/*
	 * Konstruktor zum anlegen eines Events.
	 * @param s zugehörige Strecke
	 */
	public Event(Strecke s) {
		this.strecke = s;
	}
	
	/*
	 * Konstruktor zum anlegen eines Events.
	 * @param s1 zugehörige erste Strecke
	 * @param s2 zugehörige zweite Strecke
	 */
	public Event(Strecke s1, Strecke s2) {
		this.strecke = s1;
		this.strecke2 = s2;
	}

	/*
	 *Abstrakte Methode die je nach Art des Events eine Behandlung für einen linken, rechten oder Schnittpunkt ausführt.
	 */
	public abstract void doEvent();
	
	/*
	 * Vergleich von Events anhand der Strecken
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		Event e = (Event) o;
		
		if((e.strecke.equals(strecke2) && e.strecke2.equals(strecke)) || (e.strecke.equals(strecke) && e.strecke2.equals(strecke2))) {
			return true;
		}
		else {
			return false;
		}
	}

}
