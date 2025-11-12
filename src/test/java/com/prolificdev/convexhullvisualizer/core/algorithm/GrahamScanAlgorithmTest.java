package com.prolificdev.convexhullvisualizer.core.algorithm;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.prolificdev.convexhullvisualizer.core.geometry.Point2D;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;
import org.junit.jupiter.api.Test;

public class GrahamScanAlgorithmTest {

    private final GrahamScanAlgorithm grahamScan = new GrahamScanAlgorithm();

    @Test
    void returnsEmptyCollectionsWhenLessThanThreePoints() {
        List<Point2D> pts = List.of(new Point2D(0, 0), new Point2D(1, 1));

        ConvexHullResult<Point2D> result = grahamScan.compute(pts);

        assertEquals(2, result.input().size());
        assertTrue(result.hull().isEmpty(), "Hull should be empty for less than 3 points");
        assertTrue(result.base().isEmpty(), "Base should be empty for less than 3 points");
        assertTrue(result.colinear().isEmpty(), "Colinear should be empty for less than 3 points");
    }

    @Test
    void computesTriangleHullCorrectly() {
        // triangle points in arbitrary order
        List<Point2D> pts = List.of(
                new Point2D(0, 0),
                new Point2D(2, 0),
                new Point2D(0, 2),
                new Point2D(2, 2),
                new Point2D(1, 1)
        );

        ConvexHullResult<Point2D> result = grahamScan.compute(pts);

        // input should be preserved
        assertEquals(5, result.input().size(), "Input should contain the 5 provided points");

        // resulting hull should have 4 points (the corners)
        assertEquals(4, result.hull().size(), "Hull should contain the 4 corner points");
        assertTrue(result.hull().contains(new Point2D(0, 0)), "Hull should contain point (0, 0)");
        assertTrue(result.hull().contains(new Point2D(2, 0)), "Hull should contain point (2, 0)");
        assertTrue(result.hull().contains(new Point2D(0, 2)), "Hull should contain point (0, 2)");
        assertTrue(result.hull().contains(new Point2D(2, 2)), "Hull should contain point (2, 2)");

        // base should contain the inner point
        assertEquals(1, result.base().size(), "Base should contain the inner point");
        assertTrue(result.base().contains(new Point2D(1, 1)), "Base should contain point (1, 1)");

        // colinear should be empty
        assertTrue(result.colinear().isEmpty(), "Colinear should be empty for this input");

        // algorithm metadata should be set
        assertEquals("2D", result.dimension());
        assertEquals("Graham Scan", result.algorithm());

        // computation time should be non-negative
        assertTrue(result.computationTimeMs() >= 0, "Computation time should be >= 0");
    }
}
