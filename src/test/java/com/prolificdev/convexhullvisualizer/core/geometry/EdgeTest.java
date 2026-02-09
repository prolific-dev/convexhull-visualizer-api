package com.prolificdev.convexhullvisualizer.core.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    @Test
    void reversedSwapsEndpoints() {
        Point3D a = new Point3D(0.0, 0.0, 0.0);
        Point3D b = new Point3D(1.0, 2.0, 3.0);

        Edge edge = new Edge(a, b);
        Edge reversed = edge.reversed();

        assertEquals(a, edge.a());
        assertEquals(b, edge.b());
        assertEquals(b, reversed.a(), "Reversed edge should start where the original ended");
        assertEquals(a, reversed.b(), "Reversed edge should end where the original started");
    }

    @Test
    void reversingTwiceReturnsSameEdge() {
        Point3D a = new Point3D(-1.0, 5.0, 2.0);
        Point3D b = new Point3D(4.0, -3.0, 0.5);

        Edge edge = new Edge(a, b);

        assertEquals(edge, edge.reversed().reversed(), "Reversing twice should give the original edge");
    }
}
