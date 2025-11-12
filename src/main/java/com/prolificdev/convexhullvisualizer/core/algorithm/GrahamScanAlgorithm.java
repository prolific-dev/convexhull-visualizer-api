package com.prolificdev.convexhullvisualizer.core.algorithm;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

//import java.util.Map;
//import java.util.LinkedHashMap;

import com.prolificdev.convexhullvisualizer.core.geometry.Point2D;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;

@Component("grahamScan")
public class GrahamScanAlgorithm implements ConvexHullAlgorithm<Point2D> {

    private static final int MIN_POINTS = 3;
    private static final double EPSILON = 1e-10;
    
    @Override
    public ConvexHullResult<Point2D> compute(List<Point2D> points) {

        if (points == null || points.size() < MIN_POINTS) {
            return new ConvexHullResult<>(
                List.copyOf(points),
                List.of(),
                List.of(),
                List.of(),
                "2D",
                "Graham Scan",
                0,
                null
            );
        }
        
        // Passing new ArrayList to avoid sorting on an immutable list because of List.of() in controller.
        List<Point2D> sorted = sort(new ArrayList<>(points));
        Deque<Point2D> stack = new ArrayDeque<>();
        List<Point2D> base = new ArrayList<>();

        // start timing here (includes sort). Move this line if you want to exclude sort.
        final long startNs = System.nanoTime();

        stack.push(sorted.get(0));
        stack.push(sorted.get(1));

        int i = 2;
        while (i < sorted.size()) {
            Point2D si = sorted.get(i);

            if (stack.size() == 2) {
                stack.push(si);
                i++;
                continue;
            }

            Point2D top = stack.peek();
            Point2D next = getSecondFromTop(stack);

            if (isLeftTurn(next, top, si)) {
                stack.push(si);
                i++;
            } else {
                base.add(stack.pop());
            }
        }

        // stop timing
        final long endNs = System.nanoTime();
        final long computationTimeMs = (endNs - startNs) / 1_000_000L;

        List<Point2D> hull = new ArrayList<>(stack);
        List<Point2D> colinear = new ArrayList<>();
        
        //DistinctColinearPoints distinct = extractColinearPoints(hull);

        return new ConvexHullResult<>(
            List.copyOf(points),
            base,
            colinear,
            hull,
            "2D",
            "Graham Scan",
            computationTimeMs,
            LocalDateTime.now()
        );
    }

    /** 
     private static DistinctColinearPoints extractColinearPoints(List<Point2D> hull) {
        if (hull.isEmpty()) {
            return new DistinctColinearPoints(List.of(), List.of());
        }

        Point2D p0 = hull.get(0);
        List<Point2D> colinear = new ArrayList<>();
        List<Point2D> distinctHull = new ArrayList<>();

        Map<Double, List<Point2D>> groups = new LinkedHashMap<>();

        for (Point2D p : hull) {
            double key = Math.round(p.getAngleP0() * 1e8) / 1e8;
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(p);
        }

        for (List<Point2D> group: groups.values()) {
            if (group.size() == 1) {
                Point2D only = group.get(0);
                if (!only.equals(p0)) hull.add(only);
                continue;
            }

            Point2D farthest = group.get(0);
            double maxDist = distSq(p0, farthest);
            for (Point2D p : group) {
                double d = distSq(p0, p);
                if (d > maxDist) {
                    maxDist = d;
                    farthest = p;
                }
            }
            
            if (!farthest.equals(p0)) {
                distinctHull.add(farthest);
            }
            
            for (Point2D p : group) {
                if (!p.equals(farthest) && !p.equals(p0)) {
                    colinear.add(p);
                }
            }
        }
        
        return new DistinctColinearPoints(distinctHull, colinear);
    }
    
    private static double distSq(Point2D a, Point2D b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return dx * dx + dy * dy;
    }
    
    private record DistinctColinearPoints(List<Point2D> hull, List<Point2D> colinear) {}
    */
    
    private static boolean isLeftTurn(Point2D a, Point2D b, Point2D c) {
        double crossProduct = (b.getX() - a.getX()) * (c.getY() - a.getY()) -
                              (b.getY() - a.getY()) * (c.getX() - a.getX());
        return crossProduct >= 0;
    }

    private static Point2D getSecondFromTop(Deque<Point2D> stack) {
        Iterator<Point2D> it = stack.iterator();
        if (!it.hasNext()) return null;
        it.next(); // skip top
        return it.hasNext() ? it.next() : null;
    }

    private static List<Point2D> sort(List<Point2D> points) {
       
        Point2D p0 = points.get(0);
        for (Point2D p : points) {
            if (p.getY() < p0.getY() || (p.getY() == p0.getY() && p.getX() < p0.getX())) {
                p0 = p;
            }
        }

        for (Point2D p : points) {
            p.setAngleP0(Math.atan2(p.getY() - p0.getY(), p.getX() - p0.getX()));
            p.setDistanceP0(Math.hypot(p.getX() - p0.getX(), p.getY() - p0.getY()));
        }

        points.sort(Comparator
                .comparing(Point2D::getAngleP0)
                .thenComparing(Point2D::getDistanceP0));

        double lastAngle = points.get(points.size() - 1).getAngleP0();
        int startIdx = points.size() - 1;

        while (startIdx > 0 && Math.abs(points.get(startIdx - 1).getAngleP0() - lastAngle) < EPSILON) {
            startIdx--;
        }
        
        Collections.reverse(points.subList(startIdx, points.size()));
        
        return points;
    }
}
