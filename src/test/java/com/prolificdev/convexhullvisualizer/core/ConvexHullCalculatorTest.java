package com.prolificdev.convexhullvisualizer.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import com.prolificdev.convexhullvisualizer.core.algorithm.ConvexHullAlgorithm;
import com.prolificdev.convexhullvisualizer.core.geometry.Point2D;
import com.prolificdev.convexhullvisualizer.core.geometry.Point3D;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;
import org.junit.jupiter.api.Test;

class ConvexHullCalculatorTest {

    static class Dummy2DAlgorithm implements ConvexHullAlgorithm<Point2D> {
        @Override
        public ConvexHullResult<Point2D> compute(List<Point2D> points) {
            return new ConvexHullResult<>(points, List.of(), List.of(), points, "2D", "dummy", 0, null);
        }
    }

    static class Dummy3DAlgorithm implements ConvexHullAlgorithm<Point3D> {
        @Override
        public ConvexHullResult<Point3D> compute(List<Point3D> points) {
            return new ConvexHullResult<>(points, List.of(), List.of(), points, "3D", "dummy3d", 0, null);
        }
    }

    @Test
    void throwsOnEmptyList() {
        ConvexHullCalculator calc = new ConvexHullCalculator(Map.of());

        assertThrows(IllegalArgumentException.class, () -> calc.compute(List.of()));
    }

    @Test
    void dispatchesTo2DAlgorithm() {
        Map<String, ConvexHullAlgorithm<?>> algs = Map.<String, ConvexHullAlgorithm<?>>of("grahamScan", new Dummy2DAlgorithm());
        ConvexHullCalculator calc = new ConvexHullCalculator(algs);

        List<Point2D> pts = List.of(new Point2D(0,0));
        ConvexHullResult<?> res = calc.compute(pts);

        assertEquals("2D", res.dimension());
        assertEquals(1, res.hull().size());
    }

    @Test
    void dispatchesTo3DAlgorithm() {
        Map<String, ConvexHullAlgorithm<?>> algs = Map.<String, ConvexHullAlgorithm<?>>of("quickHull", new Dummy3DAlgorithm());
        ConvexHullCalculator calc = new ConvexHullCalculator(algs);

        List<Point3D> pts = List.of(new Point3D(0,0,0));
        ConvexHullResult<?> res = calc.compute(pts);

        assertEquals("3D", res.dimension());
        assertEquals(1, res.hull().size());
    }
}
