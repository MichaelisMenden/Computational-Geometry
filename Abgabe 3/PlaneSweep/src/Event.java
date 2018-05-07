
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
