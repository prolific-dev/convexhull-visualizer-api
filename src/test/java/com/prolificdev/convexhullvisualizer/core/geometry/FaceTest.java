package com.prolificdev.convexhullvisualizer.core.geometry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FaceTest {

    private static final double EPS = 1e-9;

    @Test
    void constructorComputesNormalViaCrossProduct() {
        Point3D a = new Point3D(0.0, 0.0, 0.0);
        Point3D b = new Point3D(1.0, 0.0, 0.0);
        Point3D c = new Point3D(0.0, 1.0, 0.0);

        Face face = new Face(a, b, c);

        Point3D expected = c.subtract(a).cross(b.subtract(a));
        assertEquals(expected.getX(), face.getNormal().getX(), EPS);
        assertEquals(expected.getY(), face.getNormal().getY(), EPS);
        assertEquals(expected.getZ(), face.getNormal().getZ(), EPS);
    }

    @Test
    void distanceToReturnsSignedDistanceFromPlane() {
        Point3D a = new Point3D(0.0, 0.0, 0.0);
        Point3D b = new Point3D(2.0, 0.0, 0.0);
        Point3D c = new Point3D(0.0, 2.0, 0.0);
        Face face = new Face(a, b, c);

        Point3D above = new Point3D(0.0, 0.0, 2.0);
        Point3D below = new Point3D(0.0, 0.0, -2.0);

        double distAbove = face.distanceTo(above);
        double distBelow = face.distanceTo(below);

        assertEquals(-2.0, distAbove, EPS, "Distance should depend on the face normal orientation");
        assertEquals(2.0, distBelow, EPS);
    }

    @Test
    void degenerativeFaceReturnsZeroDistance() {
        Point3D a = new Point3D(0.0, 0.0, 0.0);
        Point3D b = new Point3D(1.0, 0.0, 0.0);
        Point3D c = new Point3D(2.0, 0.0, 0.0);
        Face face = new Face(a, b, c);

        Point3D p = new Point3D(0.0, 1.0, 1.0);
        assertEquals(0.0, face.distanceTo(p), EPS, "Degenerate faces should report zero distance");
        assertEquals(0.0, face.getNormal().magnitude(), EPS, "Normal should be zero for colinear points");
    }

    @Test
    void edgesReturnClosedLoop() {
        Point3D a = new Point3D(0.0, 0.0, 0.0);
        Point3D b = new Point3D(1.0, 0.0, 0.0);
        Point3D c = new Point3D(0.0, 1.0, 0.0);
        Face face = new Face(a, b, c);

        List<Edge> edges = face.edges();
        assertEquals(3, edges.size(), "Triangle face should expose three edges");
        assertEquals(new Edge(a, b), edges.get(0));
        assertEquals(new Edge(b, c), edges.get(1));
        assertEquals(new Edge(c, a), edges.get(2));
    }
}
