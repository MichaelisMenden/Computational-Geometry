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
	public void setBothX(float x) {
		start.x = x;
		end.x = x;
	}
	
	@Override
	public boolean equals(Object o) {
		Strecke s = (Strecke) o;
		if(Math.abs(this.getxStart() - s.getxStart()) < 0.000000001 && Math.abs(this.getyStart() - s.getyStart()) < 0.000000001 && 
				Math.abs(this.getxEnd() - s.getxEnd()) < 0.000000001 && Math.abs(this.getyEnd() - s.getyEnd()) < 0.000000001) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Start-X = " + start.x + "Start-Y = " + start.y + "End-X = " + end.x + "End-Y = " + end.y;
	}
}
