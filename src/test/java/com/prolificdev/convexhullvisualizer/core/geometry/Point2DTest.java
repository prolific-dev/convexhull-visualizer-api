package com.prolificdev.convexhullvisualizer.core.geometry;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Point2DTest {

	@Test
	public void equalsAndHashCodeBasedOnXY() {
		Point2D a = new Point2D(1.0, 2.0);
		a.setAngleP0(0.5);
		a.setDistanceP0(10.0);

		Point2D b = new Point2D(1.0, 2.0);
		b.setAngleP0(1.5);
		b.setDistanceP0(5.0);

		assertEquals(a, b, "Points with same x,y should be equal");
		assertEquals(a.hashCode(), b.hashCode(), "Hash codes should match for equal points");
	}

	@Test
	public void compareToComparesByAngleFirst() {
		Point2D p1 = new Point2D(0, 0);
		p1.setAngleP0(0.1);
		p1.setDistanceP0(5.0);

		Point2D p2 = new Point2D(0, 0);
		p2.setAngleP0(0.5);
		p2.setDistanceP0(1.0);

		assertTrue(p1.compareTo(p2) < 0, "Smaller angle should come first");
	}

	@Test
	public void compareToWhenAnglesEqualComparesByDistance() {
		Point2D p1 = new Point2D(0, 0);
		p1.setAngleP0(0.2);
		p1.setDistanceP0(1.0);

		Point2D p2 = new Point2D(0, 0);
		p2.setAngleP0(0.2);
		p2.setDistanceP0(2.0);

		assertTrue(p1.compareTo(p2) < 0, "When angles equal, smaller distance should come first");
	}

	@Test
	public void sortingRespectsCompareTo() {
		Point2D a = new Point2D(0, 0);
		a.setAngleP0(0.3);
		a.setDistanceP0(3.0);

		Point2D b = new Point2D(0, 0);
		b.setAngleP0(0.1);
		b.setDistanceP0(5.0);

		Point2D c = new Point2D(0, 0);
		c.setAngleP0(0.1);
		c.setDistanceP0(2.0);

		List<Point2D> list = new ArrayList<>();
		list.add(a);
		list.add(b);
		list.add(c);

		Collections.sort(list);

		// expected order: c (angle 0.1, dist 2.0), b (angle 0.1, dist 5.0), a (angle 0.3)
		assertSame(c, list.get(0));
		assertSame(b, list.get(1));
		assertSame(a, list.get(2));
	}

	@Test
	public void toStringHasXYFormatted() {
		Point2D p = new Point2D(1.234, 5.678);
		String expected = String.format("%f, %f", 1.234, 5.678);
		assertEquals(expected, p.toString());
	}
}
