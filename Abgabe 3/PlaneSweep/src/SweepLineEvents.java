
public class SweepLineEvents {
	
	public float x;
	
	public SweepLineEvents(float x) {
		this.x = x;
	}

	public void treatLeftEndpoint() {
		System.out.println(x);
	}
	
	public void treatRightEndpoint() {
		System.out.println(x);
	}
	
	public void treatIntersection() {
		System.out.println(x);
	}
	
}
