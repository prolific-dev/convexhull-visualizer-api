package com.prolificdev.convexhullvisualizer.core.algorithm;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        @Test
        void handlesAllColinearPoints() {
            List<Point2D> pts = List.of(
                new Point2D(-2, 0),
                new Point2D(-1, 0),
                new Point2D(0, 0),
                new Point2D(1, 0),
                new Point2D(2, 0),
                new Point2D(3, 0)
            );

            ConvexHullResult<Point2D> result = grahamScan.compute(pts);

            // only the endpoints should be in the hull
            assertEquals(6, result.input().size(), "Input should contain all provided points");
            assertEquals(2, result.hull().size(), "Hull should contain only the 2 endpoints");
            assertEquals(4, result.colinear().size(), "Colinear should contain all interior points");
            assertTrue(result.base().isEmpty(), "Base should be empty for strictly colinear points");
        }

        @Test
        void handlesDuplicatePointsGracefully() {
            List<Point2D> pts = List.of(
                new Point2D(0, 0),
                new Point2D(0, 0), // duplicate pivot
                new Point2D(1, 1),
                new Point2D(1, 1), // duplicate interior
                new Point2D(1, 1), // another duplicate interior
                new Point2D(2, 0),
                new Point2D(2, 0)  // duplicate extreme
            );

            ConvexHullResult<Point2D> result = grahamScan.compute(pts);

            // input preserved
            assertEquals(7, result.input().size(), "Input should contain all provided points (including duplicates)");

            // hull must contain the unique extreme points
            Set<Point2D> hullSet = new HashSet<>(result.hull());
            Set<Point2D> expected = Set.of(
                new Point2D(0, 0), new Point2D(2, 0), new Point2D(1, 1)
            );
            assertTrue(hullSet.containsAll(expected), "Hull must contain the extreme unique points");
            assertEquals(0, result.base().size(), "Duplicates alone should not populate base");
            assertTrue(result.colinear().isEmpty(), "Duplicates on hull edges should remain part of hull/colinear filtering");
        }

        @Test
        void preservesInputListSize() {
            List<Point2D> pts = List.of(
                new Point2D(0, 0),
                new Point2D(2, 0),
                new Point2D(0, 2),
                new Point2D(2, 2),
                new Point2D(1, 1)
            );

            ConvexHullResult<Point2D> result = grahamScan.compute(pts);

            // compute must not remove or add points to the reported input
            assertEquals(5, result.input().size(), "Input size must be preserved in the result");
        }

        @Test
        void computeNullInputReturnsEmptyResult() {
            ConvexHullResult<Point2D> result = grahamScan.compute(null);

            assertNotNull(result, "Null input should still produce a result object");
            assertTrue(result.input().isEmpty(), "Input should be empty when null is provided");
            assertTrue(result.hull().isEmpty(), "Hull should be empty when input is null");
            assertTrue(result.base().isEmpty(), "Base should be empty when input is null");
            assertTrue(result.colinear().isEmpty(), "Colinear should be empty when input is null");
        }

        @Test
        void sortKeepsFarthestPointForSameAngleGroup() {
            List<Point2D> pts = List.of(
                new Point2D(0, 0),
                new Point2D(1, 0),
                new Point2D(1, 1),
                new Point2D(2, 2),
                new Point2D(3, 3)
            );

            ConvexHullResult<Point2D> result = grahamScan.compute(pts);

            // the farthest point with the same polar angle should stay in the hull
            assertTrue(result.hull().contains(new Point2D(3, 3)), "farthest same-angle point should remain on hull");
            // all closer ones should be classified as interior/colinear instead of hull
            assertFalse(result.hull().contains(new Point2D(2, 2)), "middle same-angle point should not remain on hull");
            assertFalse(result.hull().contains(new Point2D(1, 1)), "closest same-angle point should not remain on hull");
            assertTrue(result.colinear().contains(new Point2D(2, 2)), "middle same-angle point should be tracked as colinear");
            assertTrue(result.colinear().contains(new Point2D(1, 1)), "closest same-angle point should be tracked as colinear");
        }

        @Test
        void rightTurnPointsMoveToBase() {
            // arrange points so that a genuine right turn occurs when processing the fourth point
            List<Point2D> pts = List.of(
                new Point2D(0, 0),
                new Point2D(4, 0),
                new Point2D(3, 1),
                new Point2D(5, -3), // clearly below the previous edge to force a right turn
                new Point2D(6, 3)
            );

            ConvexHullResult<Point2D> result = grahamScan.compute(pts);

            assertTrue(result.base().contains(new Point2D(3, 1)), "right turn should move previous top to base");
            assertFalse(result.hull().contains(new Point2D(3, 1)), "popped point should not stay on hull");
        }
}
