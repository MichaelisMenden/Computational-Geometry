
public abstract class Event {
	public Strecke strecke;
	public Strecke strecke2;
	
	public Event(Strecke s) {
		this.strecke = s;
	}
	
	public Event(Strecke s1, Strecke s2) {
		this.strecke = s1;
		this.strecke2 = s2;
	}


	public abstract void doEvent();

}
