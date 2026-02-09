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

import com.prolificdev.convexhullvisualizer.core.geometry.Point2D;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;

@Component("grahamScan")
public class GrahamScanAlgorithm implements ConvexHullAlgorithm<Point2D> {

    private static final int MIN_POINTS = 3;
    private static final double EPSILON = 1e-10;
    
    @Override
    public ConvexHullResult<Point2D> compute(List<Point2D> points) {

        if (!isComputable(points)) {
            return buildTrivialResult(points);
        }
        
        // Passing new ArrayList to avoid sorting on an immutable list because of List.of() in controller.
        List<Point2D> sorted = sort(new ArrayList<>(points));
        Deque<Point2D> stack = new ArrayDeque<>();
        List<Point2D> base = new ArrayList<>();

        // start timing here (includes sort). Move this line if you want to exclude sort.
        final long startNs = System.nanoTime();
        constructHull(sorted, stack, base);

        // stop timing
        final long endNs = System.nanoTime();
        final long computationTimeMs = (endNs - startNs) / 1_000_000L;

        List<Point2D> hull = normalizeHull(stack, sorted.get(0));
        List<Point2D> colinear = new ArrayList<>();
        hull = filterInteriorColinear(hull, colinear);
        collectEdgeColinear(points, hull, colinear);

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

    private static double crossProduct(Point2D a, Point2D b, Point2D c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) -
               (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    private static double distanceSquared(Point2D a, Point2D b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        return dx * dx + dy * dy;
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

    private static boolean isComputable(List<Point2D> points) {
        return points != null && points.size() >= MIN_POINTS;
    }

    private static ConvexHullResult<Point2D> buildTrivialResult(List<Point2D> points) {
        List<Point2D> safePoints = points == null ? List.of() : List.copyOf(points);
        return new ConvexHullResult<>(
            safePoints,
            List.of(),
            List.of(),
            List.of(),
            "2D",
            "Graham Scan",
            0,
            null
        );
    }

    private static void constructHull(List<Point2D> sorted, Deque<Point2D> stack, List<Point2D> base) {
        stack.push(sorted.get(0));
        stack.push(sorted.get(1));

        int i = 2;
        while (i < sorted.size()) {
            Point2D candidate = sorted.get(i);

            if (stack.size() < 2) {
                stack.push(candidate);
                i++;
                continue;
            }

            Point2D top = stack.peek();
            Point2D next = getSecondFromTop(stack);
            double cross = crossProduct(next, top, candidate);

            if (cross > EPSILON) {
                stack.push(candidate);
                i++;
            } else if (cross < -EPSILON) {
                base.add(stack.pop());
            } else {
                double candidateDist = distanceSquared(next, candidate);
                double topDist = distanceSquared(next, top);

                if (candidateDist > topDist) {
                    stack.pop();
                } else {
                    i++;
                }
            }
        }
    }

    private static List<Point2D> normalizeHull(Deque<Point2D> stack, Point2D pivot) {
        List<Point2D> hull = new ArrayList<>(stack);
        Collections.reverse(hull);

        if (pivot != null) {
            int idx = hull.indexOf(pivot);
            if (idx > 0) Collections.rotate(hull, -idx);
        }
        return hull;
    }

    private static List<Point2D> filterInteriorColinear(List<Point2D> hull, List<Point2D> colinear) {
        if (hull.size() < 3) {
            return hull;
        }

        List<Point2D> filtered = new ArrayList<>();
        int n = hull.size();
        for (int idx = 0; idx < n; idx++) {
            Point2D prev = hull.get((idx - 1 + n) % n);
            Point2D curr = hull.get(idx);
            Point2D next = hull.get((idx + 1) % n);

            double cross = (next.getX() - prev.getX()) * (curr.getY() - prev.getY()) -
                           (next.getY() - prev.getY()) * (curr.getX() - prev.getX());

            if (Math.abs(cross) <= EPSILON) {
                double vx = next.getX() - prev.getX();
                double vy = next.getY() - prev.getY();
                double proj = (curr.getX() - prev.getX()) * vx + (curr.getY() - prev.getY()) * vy;
                double lenSq = vx * vx + vy * vy;
                if (proj > EPSILON && proj < lenSq - EPSILON) {
                    if (!colinear.contains(curr)) {
                        colinear.add(curr);
                    }
                    continue;
                }
            }
            filtered.add(curr);
        }
        return filtered;
    }

    private static void collectEdgeColinear(List<Point2D> points, List<Point2D> hull, List<Point2D> colinear) {
        java.util.Set<Point2D> hullSet = new java.util.HashSet<>(hull);
        for (Point2D p : points) {
            if (hullSet.contains(p)) {
                continue;
            }
            for (int j = 0; j < hull.size(); j++) {
                Point2D a = hull.get(j);
                Point2D b = hull.get((j + 1) % hull.size());
                double cross = (b.getX() - a.getX()) * (p.getY() - a.getY()) -
                               (b.getY() - a.getY()) * (p.getX() - a.getX());
                if (Math.abs(cross) <= EPSILON) {
                    double vx = b.getX() - a.getX();
                    double vy = b.getY() - a.getY();
                    double proj = (p.getX() - a.getX()) * vx + (p.getY() - a.getY()) * vy;
                    double lenSq = vx * vx + vy * vy;
                    if (proj > EPSILON && proj < lenSq - EPSILON && !colinear.contains(p)) {
                        colinear.add(p);
                    }
                }
            }
        }
    }
}
