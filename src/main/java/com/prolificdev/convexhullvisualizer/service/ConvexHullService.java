package com.prolificdev.convexhullvisualizer.service;

import java.util.List;
import java.util.HashSet;

import org.springframework.stereotype.Service;


import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;
import com.prolificdev.convexhullvisualizer.core.ConvexHullCalculator;
import com.prolificdev.convexhullvisualizer.core.geometry.Point2D;
import com.prolificdev.convexhullvisualizer.core.geometry.Point3D;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConvexHullService {

    private final ConvexHullCalculator convexHullCalculator;

    public ConvexHullResult<?> compute(List<String> points) {
        
        ConvexHullResult<?> result;
        String firstLine;
        String[] parts;

        if (!isValidPointList(points)) {
            throw new IllegalArgumentException("Invalid point list");
        }
        
        firstLine = points.get(0);
        parts = firstLine.split(",");

        if (parts.length == 2) {
            List<Point2D> point2DList = parse2DPoints(points);

            result = convexHullCalculator.compute(new HashSet<Point2D>(point2DList).stream().toList());

            return result;
        } 
        
        if (parts.length == 3) {
            List<Point3D> point3DList = parse3DPoints(points);

            result = convexHullCalculator.compute(new HashSet<Point3D>(point3DList).stream().toList());
            return result;
        }
        
        throw new IllegalArgumentException("Invalid point format");
    }

    private List<Point2D> parse2DPoints(List<String> raw) {
        return raw.stream()
                .map(line -> {
                    String[] parts = line.split(",");
                    double x = Double.parseDouble(parts[0].trim());
                    double y = Double.parseDouble(parts[1].trim());
                    return new Point2D(x, y);
                })
                .toList();
    }

    private List<Point3D> parse3DPoints(List<String> raw) {
        return raw.stream()
                .map(line -> {
                    String[] parts = line.split(",");
                    double x = Double.parseDouble(parts[0].trim());
                    double y = Double.parseDouble(parts[1].trim());
                    double z = Double.parseDouble(parts[2].trim());
                    return new Point3D(x, y, z);
                })
                .toList();
    }

    private boolean isValidPointList(List<String> points) {
    if (points == null || points.isEmpty()) return false;

    int dimension = points.get(0).split(",").length;
    if (dimension != 2 && dimension != 3) return false;

    for (String line : points) {
        String[] parts = line.split(",");
        if (parts.length != dimension) return false;
        try {
            for (String part : parts) {
                Double.parseDouble(part.trim());
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    return true;
}
}
