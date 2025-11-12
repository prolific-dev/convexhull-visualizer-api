package com.prolificdev.convexhullvisualizer.core;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.prolificdev.convexhullvisualizer.core.geometry.Point2D;
import com.prolificdev.convexhullvisualizer.core.geometry.Point3D;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;
import com.prolificdev.convexhullvisualizer.core.algorithm.ConvexHullAlgorithm;

@Component
public class ConvexHullCalculator {

    private final Map<String, ConvexHullAlgorithm<?>> algorithms;

    public ConvexHullCalculator(Map<String, ConvexHullAlgorithm<?>> algorithms) {
        this.algorithms = algorithms;
    }

    public ConvexHullResult<?> compute(List<?> points) {

        if (points.isEmpty()) {
            throw new IllegalArgumentException("Point list is empty");
        }

        Object firstPoint = points.get(0);

        if (firstPoint instanceof Point2D) {
            @SuppressWarnings("unchecked")
            ConvexHullAlgorithm<Point2D> algorithm = (ConvexHullAlgorithm<Point2D>) algorithms.get("grahamScan");
            return algorithm.compute((List<Point2D>) points);
        } else if (firstPoint instanceof Point3D) {
            @SuppressWarnings("unchecked")
            ConvexHullAlgorithm<Point3D> algorithm = (ConvexHullAlgorithm<Point3D>) algorithms.get("quickHull3D");
            return algorithm.compute((List<Point3D>) points);
        } else {
            throw new IllegalArgumentException("Unsupported point type: " + firstPoint.getClass().getName());
        }
    }
}
