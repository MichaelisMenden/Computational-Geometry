import java.awt.Point;
import java.awt.geom.Point2D;


public class Strecke {
	private Point2D.Float start = new Point2D.Float();
	private Point2D.Float end = new Point2D.Float();
	
	public Strecke(float xStart, float yStart, float xEnd, float yEnd) {
		start.x = xStart;
		start.y = yStart;
		end.x = xEnd;
		end.y = yEnd;
	}
	public Point2D.Float getStartPoint() {
		return start;
	}
	public Point2D.Float getEndPoint() {
		return end;
	}

	public float getxStart() {
		return start.x;
	}

	public float getyStart() {
		return start.y;
	}

	public float getxEnd() {
		return end.x;
	}

	public float getyEnd() {
		return end.y;
	}
	
	@Override
	public boolean equals(Object o) {
		Strecke s = (Strecke) o;
		if(this.getxStart() - s.getxStart() < 0.00001 && this.getyStart() - s.getyStart() < 0.00001 && 
				this.getxEnd() - s.getxEnd() < 0.00001 && this.getyEnd() - s.getyEnd() < 0.00001) {
			return true;
		}
		return false;
	}
}
