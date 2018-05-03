import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;


public class TestIntersection {

	@Test
	public void test() {
		IntersectingEdgeChecker iec = new IntersectingEdgeChecker();
		Strecke s1 = new Strecke(0,6,23.36f,1.48f);
		Strecke s2 = new Strecke(1,5,2.22f,6.14f);
		Point2D.Float p = iec.getIntersectionPoint(s1, s2);
		System.out.println(p.getX() + " und " + p.getY());
	}

}
