package com.prolificdev.convexhullvisualizer.core.algorithm;

import com.prolificdev.convexhullvisualizer.core.geometry.Point3D;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component("quickHull3D")
public class QuickHull3DAlgorithm implements ConvexHullAlgorithm<Point3D> {

    //private static final double EPS = 1e-9;

    @Override
    public ConvexHullResult<Point3D> compute(List<Point3D> points) {

        
        if (points == null || points.size() < 4) {
            return emptyResult(points);
        }

        final long start = System.nanoTime();
        List<Point3D> pts = new ArrayList<>(new HashSet<>(points)); // remove duplicates


        boolean coplanar = arePointsCoplanar(pts);
        if (coplanar) {
            return emptyResult(points);
        }

        Point3D minX = Collections.min(pts, Comparator.comparingDouble(Point3D::getX));
        Point3D maxX = Collections.max(pts, Comparator.comparingDouble(Point3D::getX));

        Point3D p1 = minX;
        Point3D p2 = maxX;
        Point3D p3 = farthestPointFromLine(pts, p1, p2);
        Point3D p4 = farthestPointFromPlane(pts, p1, p2, p3);

        if (p3 == null || p4 == null) {
            return emptyResult(points);
        }

        List<Face> faces = new ArrayList<>();
        faces.add(new Face(p1, p2, p3));
        faces.add(new Face(p1, p3, p4));
        faces.add(new Face(p1, p4, p2));
        faces.add(new Face(p2, p4, p3));

        for (Point3D p : pts) {
            if (p.equals(p1) || p.equals(p2) || p.equals(p3) || p.equals(p4)) continue;
            addPointToHull(p, faces);
        }

        Set<Point3D> hullPoints = new HashSet<>();
        for (Face f : faces) {
            hullPoints.add(f.a);
            hullPoints.add(f.b);
            hullPoints.add(f.c);
        }

        final long timeMs = (System.nanoTime() - start) / 1_000_000L;

        return new ConvexHullResult<>(
                List.copyOf(points),
                List.of(),                   // base
                List.of(),                   // colinear
                new ArrayList<>(hullPoints), // hull
                "3D",
                "QuickHull 3D",
                timeMs,
                LocalDateTime.now()
        );
    }

    private static ConvexHullResult<Point3D> emptyResult(List<Point3D> input) {
        return new ConvexHullResult<>(
                input == null ? List.of() : List.copyOf(input),
                List.of(), // base
                List.of(), // colinear
                List.of(), // hull
                "3D",
                "QuickHull 3D",
                0,
                LocalDateTime.now()
        );
    }

    private static boolean arePointsCoplanar(List<Point3D> points) {
        if (points.size() < 4) return true;

        Point3D a = points.get(0);
        Point3D b = points.get(1);
        Point3D c = points.get(2);

        Point3D normal = b.subtract(a).cross(c.subtract(a));
        double normMag = normal.magnitude();

        // first three points are collinear
        if (normMag < 1e-12) return true;

        // Check distance of every other point to the plane
        for (int i = 3; i < points.size(); i++) {
            Point3D p = points.get(i);
            double dist = Math.abs(normal.dot(p.subtract(a))) / normMag;
            if (dist > 1e-9) {
                return false; // not coplanar -> true 3D set
            }
        }
        return true; // all points coplanar
    }

    private static Point3D farthestPointFromLine(List<Point3D> pts, Point3D a, Point3D b) {
        double maxDist = -1.0;
        Point3D farthest = null;
        for (Point3D p : pts) {
            double dist = distancePointToLine(p, a, b);
            if (dist > maxDist + 1e-12) {
                maxDist = dist;
                farthest = p;
            }
        }
        return farthest;
    }

    private static Point3D farthestPointFromPlane(List<Point3D> pts, Point3D a, Point3D b, Point3D c) {
        double maxDist = -1.0;
        Point3D farthest = null;
        for (Point3D p : pts) {
            double dist = Math.abs(signedDistanceToPlane(p, a, b, c));
            if (dist > maxDist + 1e-12) {
                maxDist = dist;
                farthest = p;
            }
        }
        return farthest;
    }

    private static double distancePointToLine(Point3D p, Point3D a, Point3D b) {
        Point3D ab = b.subtract(a);
        Point3D ap = p.subtract(a);
        Point3D cross = ab.cross(ap);
        return cross.magnitude() / ab.magnitude();
    }

    private static double signedDistanceToPlane(Point3D p, Point3D a, Point3D b, Point3D c) {
        Point3D normal = b.subtract(a).cross(c.subtract(a));
        double numerator = normal.dot(p.subtract(a));
        double denominator = normal.magnitude();
        return denominator == 0 ? 0 : numerator / denominator;
    }

    private static void addPointToHull(Point3D p, List<Face> faces) {
        List<Face> visible = new ArrayList<>();
        for (Face f : faces) {
            if (f.distanceTo(p) > 1e-9) visible.add(f);
        }
        if (visible.isEmpty()) return; // inside hull

        faces.removeAll(visible);

        Set<Edge> horizon = new HashSet<>();
        for (Face f : visible) {
            for (Edge e : f.edges()) {
                if (!isSharedEdge(e, visible)) horizon.add(e);
            }
        }

        for (Edge e : horizon) {
            faces.add(new Face(e.a(), e.b(), p));
        }
    }

    private static boolean isSharedEdge(Edge edge, List<Face> faces) {
        int count = 0;
        for (Face f : faces) {
            for (Edge e : f.edges()) {
                if (edge.equals(e) || edge.equals(e.reversed())) count++;
            }
        }
        return count > 1;
    }

    private record Edge(Point3D a, Point3D b) {
        Edge reversed() { return new Edge(b, a); }
    }

    private static class Face {
        Point3D a, b, c;
        Point3D normal;

        Face(Point3D a, Point3D b, Point3D c) {
            this.a = a; 
            this.b = b; 
            this.c = c;
            this.normal = c.subtract(a).cross(b.subtract(a)); // flipped cross
        }

        double distanceTo(Point3D p) {
            double denom = normal.magnitude();
            return denom == 0 ? 0 : normal.dot(p.subtract(a)) / denom;
        }

        List<Edge> edges() {
            return List.of(new Edge(a, b), new Edge(b, c), new Edge(c, a));
        }
    }
}
