import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;
/*
 * Testklasse für IntersectionEdgeChecker
 * @Author Patrick Burger, Michael Wimmer
 */

public class TestIntersectLines {
	IntersectingEdgeChecker iec = new IntersectingEdgeChecker();
	
	@Test
	public void testccw() {
		//Punkte gegen den Uhrzeigersinn
		Point2D.Double p1 = new Point2D.Double(0,0);
		Point2D.Double p2 = new Point2D.Double(1,1);
		Point2D.Double p3 = new Point2D.Double(0,1);
		assertTrue(iec.ccw(p1,p2,p3) < 0); 
		//Punkte im Uhrzeigersinn
		p1 = new Point2D.Double(0,0);
		p2 = new Point2D.Double(0,1);
		p3 = new Point2D.Double(1,1);
		assertTrue(iec.ccw(p1,p2,p3) > 0); 
		//Punkte auf einer Linie
		p1 = new Point2D.Double(0,0);
		p2 = new Point2D.Double(1,1);
		p3 = new Point2D.Double(2,2);
		assertTrue(iec.ccw(p1,p2,p3) == 0);
		//Punkte liegen aufeinander
		p1 = new Point2D.Double(0,0);
		p2 = new Point2D.Double(0,0);
		p3 = new Point2D.Double(0,0);
		assertTrue(iec.ccw(p1,p2,p3) == 0);
	}
	
	@Test
	public void testdoIntersect() {	
		//Test kolinear und Überlappend
		//Strecke s1 = new Strecke(0d,0d,2d,2d);
		//Strecke s2 = new Strecke(1d,1d,3d,3d);
		Strecke s1 = new Strecke(10.0,10.0,10.0,10.0);
		Strecke s2 = new Strecke(10.0,10.0,10.0,10.1);
		int a = iec.doIntersect(s1, s2);
		int b = iec.doIntersect(s2, s1);
		assertTrue(iec.doIntersect(s1, s2) == 0);
		assertTrue(iec.doIntersect(s2, s1) == 0);
		//Test kolinear und eine Strecke komplett in anderer Strecke
		s1 = new Strecke(0d,0d,3d,3d);
		s2 = new Strecke(1d,1d,2d,2d);
		assertTrue(iec.doIntersect(s1, s2) == 0);
		assertTrue(iec.doIntersect(s2, s1) == 0);
		//Test überschneidende Linien
		s1 = new Strecke(0d,0d,3d,3d);
		s2 = new Strecke(0d,1d,1d,0d);
		assertTrue(iec.doIntersect(s1, s2) == 1);
		assertTrue(iec.doIntersect(s2, s1) == 1);
		//Test nicht überschneidende Linien
		s1 = new Strecke(0d,0d,0d,3d);
		s2 = new Strecke(1d,0d,4d,0d);
		assertTrue(iec.doIntersect(s1, s2) == -1);
		assertTrue(iec.doIntersect(s2, s1) == -1);
		//Ein Punkt liegt auf Linie
		s1 = new Strecke(0d,0d,3d,3d);
		s2 = new Strecke(1d,1d,2d,0d);
		assertTrue(iec.doIntersect(s1, s2) == 1);
		assertTrue(iec.doIntersect(s2, s1) == 1);
	}
	
}


