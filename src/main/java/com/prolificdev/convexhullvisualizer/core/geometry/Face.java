package com.prolificdev.convexhullvisualizer.core.geometry;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Face {
    public Point3D a;
    public Point3D b;
    public Point3D c;
    public Point3D normal;

    public Face(Point3D a, Point3D b, Point3D c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.normal = computeNormal(a, b, c);
    }

    private Point3D computeNormal(Point3D a, Point3D b, Point3D c) {
        return c.subtract(a).cross(b.subtract(a));
    }

    public double distanceTo(Point3D p) {
        double denom = this.normal.magnitude();
        return denom == 0 ? 0 : this.normal.dot(p.subtract(this.a)) / denom;
    }

    public List<Edge> edges() {
        return List.of(new Edge(this.a, this.b), new Edge(this.b, this.c), new Edge(this.c, this.a));
    }
}
