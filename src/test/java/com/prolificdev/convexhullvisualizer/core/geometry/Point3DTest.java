package com.prolificdev.convexhullvisualizer.core.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Point3DTest {

	private static final double EPS = 1e-9;

	@Test
	public void equalsAndHashCodeBasedOnXYZ() {
		Point3D a = new Point3D(1.0, 2.0, 3.0);
		Point3D b = new Point3D(1.0, 2.0, 3.0);

		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void subtractReturnsExpectedVector() {
		Point3D p1 = new Point3D(2.0, 3.0, 4.0);
		Point3D p2 = new Point3D(1.0, 1.0, 1.0);

		Point3D r = p1.subtract(p2);

		assertEquals(1.0, r.getX(), EPS);
		assertEquals(2.0, r.getY(), EPS);
		assertEquals(3.0, r.getZ(), EPS);
	}

	@Test
	public void crossComputesRightHandRule() {
		Point3D i = new Point3D(1.0, 0.0, 0.0);
		Point3D j = new Point3D(0.0, 1.0, 0.0);

		Point3D k = i.cross(j);

		assertEquals(0.0, k.getX(), EPS);
		assertEquals(0.0, k.getY(), EPS);
		assertEquals(1.0, k.getZ(), EPS);
	}

	@Test
	public void dotReturnsScalarProduct() {
		Point3D a = new Point3D(1.0, 2.0, 3.0);
		Point3D b = new Point3D(4.0, 5.0, 6.0);

		assertEquals(32.0, a.dot(b), EPS);
	}

	@Test
	public void magnitudeComputesLength() {
		Point3D v = new Point3D(1.0, 2.0, 2.0);
		assertEquals(3.0, v.magnitude(), EPS);
	}

	@Test
	public void toStringFormatsThreeDecimals() {
		Point3D p = new Point3D(1.0, 2.3456, -3.21);
		String expected = String.format("(%.3f, %.3f, %.3f)", 1.0, 2.3456, -3.21);
		assertEquals(expected, p.toString());
	}
}
