import java.awt.Point;
import java.awt.geom.Point2D;


public class Strecke {
	private Point2D.Double start = new Point2D.Double();
	private Point2D.Double end = new Point2D.Double();
	
	public Strecke(double xStart, double yStart, double xEnd, double yEnd) {
		start.x = xStart;
		start.y = yStart;
		end.x = xEnd;
		end.y = yEnd;
	}
	public Point2D.Double getStartPoint() {
		return start;
	}
	public Point2D.Double getEndPoint() {
		return end;
	}

	public double getxStart() {
		return start.x;
	}

	public double getyStart() {
		return start.y;
	}

	public double getxEnd() {
		return end.x;
	}

	public double getyEnd() {
		return end.y;
	}
	public void setBothX(double x) {
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
		//return "Start-X= " + start.x + "  Start-Y= " + start.y + "  End-X= " + end.x + "  End-Y= " + end.y;
		return "Strecke(Punkt[{" + start.x + "," + start.y + "}],Punkt[{" + end.x + "," + end.y + "}])";
	}
}
