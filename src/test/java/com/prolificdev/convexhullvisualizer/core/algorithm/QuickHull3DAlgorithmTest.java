package com.prolificdev.convexhullvisualizer.core.algorithm;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.prolificdev.convexhullvisualizer.core.geometry.Point3D;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;
import org.junit.jupiter.api.Test;

public class QuickHull3DAlgorithmTest {

    private final QuickHull3DAlgorithm quickHull = new QuickHull3DAlgorithm();

    @Test
    void returnsEmptyCollectionsWhenLessThanFourPoints() {
        List<Point3D> pts = List.of(
                new Point3D(0, 0, 0),
                new Point3D(1, 0, 0),
                new Point3D(0, 1, 0)
        );

        ConvexHullResult<Point3D> result = quickHull.compute(pts);

        assertEquals(3, result.input().size());
        assertTrue(result.hull().isEmpty(), "Hull should be empty for less than 4 points");
        assertTrue(result.base().isEmpty(), "Base should be empty for less than 4 points");
        assertTrue(result.colinear().isEmpty(), "Colinear should be empty for less than 4 points");
    }

    @Test
    void computesTetrahedronHullCorrectly() {
        // 4 non-coplanar points forming a tetrahedron + one interior point
        Point3D p1 = new Point3D(0, 0, 0);
        Point3D p2 = new Point3D(1, 0, 0);
        Point3D p3 = new Point3D(0, 1, 0);
        Point3D p4 = new Point3D(0, 0, 1);
        Point3D inner = new Point3D(0.1, 0.1, 0.1);

        List<Point3D> pts = List.of(p1, p2, p3, p4, inner);

        ConvexHullResult<Point3D> result = quickHull.compute(pts);

        // input preserved
        assertEquals(5, result.input().size(), "Input should contain the 5 provided points");

        // hull should contain the 4 tetrahedron vertices
        assertEquals(4, result.hull().size(), "Hull should contain the 4 tetrahedron vertices");
        assertTrue(result.hull().contains(p1));
        assertTrue(result.hull().contains(p2));
        assertTrue(result.hull().contains(p3));
        assertTrue(result.hull().contains(p4));

        // inner point should not be on hull
        assertFalse(result.hull().contains(inner), "Inner point should not be part of the hull");

        // metadata
        assertEquals("3D", result.dimension());
        assertEquals("QuickHull 3D", result.algorithm());
        assertTrue(result.computationTimeMs() >= 0, "Computation time should be >= 0");
    }

    @Test
    void returnsEmptyForCoplanarPoints() {
    List<Point3D> pts = List.of(
        new Point3D(0,0,0),
        new Point3D(1,0,0),
        new Point3D(0,1,0),
        new Point3D(1,1,0)
    );
    ConvexHullResult<Point3D> result = quickHull.compute(pts);
    assertTrue(result.hull().isEmpty(), "Coplanar points should yield no 3D hull");
}


    @Test
    void handlesDuplicatePointsGracefully() {
        // duplicates of the tetrahedron corners
        Point3D p1 = new Point3D(0, 0, 0);
        Point3D p2 = new Point3D(1, 0, 0);
        Point3D p3 = new Point3D(0, 1, 0);
        Point3D p4 = new Point3D(0, 0, 1);

        List<Point3D> pts = List.of(p1, p2, p3, p4, p1, p2, p3);

        ConvexHullResult<Point3D> result = quickHull.compute(pts);

        // duplicates should not increase hull size
        assertEquals(4, result.hull().size(), "Hull should contain the 4 distinct corner points despite duplicates");
        assertTrue(result.hull().contains(p1));
        assertTrue(result.hull().contains(p2));
        assertTrue(result.hull().contains(p3));
        assertTrue(result.hull().contains(p4));
    }
}