package com.prolificdev.convexhullvisualizer.core.geometry;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Point3D {
    @EqualsAndHashCode.Include
    private double x;
    @EqualsAndHashCode.Include
    private double y;
    @EqualsAndHashCode.Include
    private double z;

    public Point3D subtract(Point3D other) {
        return new Point3D(x - other.x, y - other.y, z - other.z);
    }

    public Point3D cross(Point3D other) {
        return new Point3D(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
    }

    public double dot(Point3D other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public String toString() {
        return String.format("(%.3f, %.3f, %.3f)", x, y, z);
    }
}
