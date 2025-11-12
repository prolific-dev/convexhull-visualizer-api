package com.prolificdev.convexhullvisualizer.core.geometry;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Point2D implements Comparable<Point2D> {

    @EqualsAndHashCode.Include
    private double x;
    @EqualsAndHashCode.Include
    private double y;

    private double angleP0;
    private double distanceP0;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Point2D other) {
        int cmp = Double.compare(this.angleP0, other.angleP0);
        if (cmp != 0) return cmp;
        return Double.compare(this.distanceP0, other.distanceP0);
    }

    @Override
    public String toString() {
        return String.format("%f, %f", x, y);
    }
}