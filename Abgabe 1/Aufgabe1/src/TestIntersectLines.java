import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;


public class TestIntersectLines {
	IntersectingEdgeChecker iec = new IntersectingEdgeChecker();
	
	@Test
	public void testccw() {
		//Punkte gegen den Uhrzeigersinn
		Point2D.Float p1 = new Point2D.Float(0,0);
		Point2D.Float p2 = new Point2D.Float(1,1);
		Point2D.Float p3 = new Point2D.Float(0,1);
		assertTrue(iec.ccw(p1,p2,p3) < 0); 
		//Punkte im Uhrzeigersinn
		p1 = new Point2D.Float(0,0);
		p2 = new Point2D.Float(0,1);
		p3 = new Point2D.Float(1,1);
		assertTrue(iec.ccw(p1,p2,p3) > 0); 
		//Punkte auf einer Linie
		p1 = new Point2D.Float(0,0);
		p2 = new Point2D.Float(1,1);
		p3 = new Point2D.Float(2,2);
		assertTrue(iec.ccw(p1,p2,p3) == 0);
		//Punkte liegen aufeinander
		p1 = new Point2D.Float(0,0);
		p2 = new Point2D.Float(0,0);
		p3 = new Point2D.Float(0,0);
		assertTrue(iec.ccw(p1,p2,p3) == 0);
	}
	
	@Test
	public void testdoIntersect() {
		//Test kolinear und Überlappend
		Strecke s1 = new Strecke(0,0,2,2);
		Strecke s2 = new Strecke(1,1,3,3);
		assertTrue(iec.doIntersect(s1, s2) == 0);
		assertTrue(iec.doIntersect(s2, s1) == 0);
		//Test kolinear und eine Strecke komplett in anderer Strecke
		s1 = new Strecke(0,0,3,3);
		s2 = new Strecke(1,1,2,2);
		assertTrue(iec.doIntersect(s1, s2) == 0);
		assertTrue(iec.doIntersect(s2, s1) == 0);
		//Test überschneidende Linien
		s1 = new Strecke(0,0,3,3);
		s2 = new Strecke(0,1,1,0);
		assertTrue(iec.doIntersect(s1, s2) == 1);
		assertTrue(iec.doIntersect(s2, s1) == 1);
		//Test nicht überschneidende Linien
		s1 = new Strecke(0,0,0,3);
		s2 = new Strecke(1,0,4,0);
		assertTrue(iec.doIntersect(s1, s2) == -1);
		assertTrue(iec.doIntersect(s2, s1) == -1);
		//Ein Punkt liegt auf Linie
		s1 = new Strecke(0,0,3,3);
		s2 = new Strecke(1,1,2,0);
		assertTrue(iec.doIntersect(s1, s2) == 1);
		assertTrue(iec.doIntersect(s2, s1) == 1);
	}
	
}


