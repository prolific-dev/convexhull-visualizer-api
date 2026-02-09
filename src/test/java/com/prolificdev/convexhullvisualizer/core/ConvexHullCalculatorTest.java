package com.prolificdev.convexhullvisualizer.core;

import com.prolificdev.convexhullvisualizer.core.algorithm.ConvexHullAlgorithm;
import com.prolificdev.convexhullvisualizer.core.geometry.Point2D;
import com.prolificdev.convexhullvisualizer.core.geometry.Point3D;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConvexHullCalculatorTest {

    @Test
    void compute_withPoint2D_usesGrahamScan() {
        // arrange
        ConvexHullAlgorithm<Point2D> graham = mock(ConvexHullAlgorithm.class);
        ConvexHullAlgorithm<Point3D> quick3d = mock(ConvexHullAlgorithm.class);

        Map<String, ConvexHullAlgorithm<?>> algorithms = new HashMap<>();
        algorithms.put("grahamScan", graham);
        algorithms.put("quickHull3D", quick3d);

        ConvexHullCalculator calculator = new ConvexHullCalculator(algorithms);

        List<Point2D> points = List.of(new Point2D(0, 0), new Point2D(1, 0), new Point2D(0, 1));

        ConvexHullResult<Point2D> expected = new ConvexHullResult<>(
                List.copyOf(points),
                List.of(points.get(0)),
                List.of(),
                List.of(points.get(0), points.get(1)),
                "2D",
                "graham",
                1L,
                LocalDateTime.now()
        );

        when(graham.compute(points)).thenReturn(expected);

        // act
        ConvexHullResult<?> result = calculator.compute(points);

        // assert
        assertNotNull(result);
        assertEquals(expected.hull(), result.hull());
        assertEquals("2D", result.dimension());
    }

    @Test
    void compute_withPoint3D_usesQuickHull3D() {
        // arrange
        ConvexHullAlgorithm<Point2D> graham = mock(ConvexHullAlgorithm.class);
        ConvexHullAlgorithm<Point3D> quick3d = mock(ConvexHullAlgorithm.class);

        Map<String, ConvexHullAlgorithm<?>> algorithms = new HashMap<>();
        algorithms.put("grahamScan", graham);
        algorithms.put("quickHull3D", quick3d);

        ConvexHullCalculator calculator = new ConvexHullCalculator(algorithms);

        List<Point3D> points = List.of(new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(0, 1, 0));

        ConvexHullResult<Point3D> expected = new ConvexHullResult<>(
                List.copyOf(points),
                List.of(points.get(0)),
                List.of(),
                List.of(points.get(0), points.get(1)),
                "3D",
                "quick",
                2L,
                LocalDateTime.now()
        );

        when(quick3d.compute(points)).thenReturn(expected);

        // act
        ConvexHullResult<?> result = calculator.compute(points);

        // assert
        assertNotNull(result);
        assertEquals(expected.hull(), result.hull());
        assertEquals("3D", result.dimension());
    }

    @Test
    void compute_emptyList_throws() {
        Map<String, ConvexHullAlgorithm<?>> algorithms = new HashMap<>();
        ConvexHullCalculator calculator = new ConvexHullCalculator(algorithms);

        assertThrows(IllegalArgumentException.class, () -> calculator.compute(List.of()));
    }
}
