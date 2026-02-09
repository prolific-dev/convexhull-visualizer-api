package com.prolificdev.convexhullvisualizer.core.geometry;

public record Edge(Point3D a, Point3D b) {
    public Edge reversed() {
        return new Edge(b, a);
    }
}
