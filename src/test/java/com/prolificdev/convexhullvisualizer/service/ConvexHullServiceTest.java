package com.prolificdev.convexhullvisualizer.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import com.prolificdev.convexhullvisualizer.core.ConvexHullCalculator;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;
import org.junit.jupiter.api.Test;

class ConvexHullServiceTest {

    static class DummyCalculator extends ConvexHullCalculator {
        public DummyCalculator() {
            super(Map.of());
        }

        @Override
        public ConvexHullResult<?> compute(List<?> points) {
            // echo back points as hull
            return new ConvexHullResult<>(List.copyOf(points), List.copyOf(points), List.of(), List.copyOf(points), "2D", "dummy", 0, null);
        }
    }

    @Test
    void parses2DPointsAndComputes() {
        var service = new ConvexHullService(new DummyCalculator());

        List<String> raw = List.of("0,0", "1,0", "0,1");

        var res = service.compute(raw);

        assertNotNull(res);
        assertEquals("2D", res.dimension());
        assertEquals(3, res.hull().size());
    }

    @Test
    void rejectsInvalidInput() {
        var service = new ConvexHullService(new DummyCalculator());

        List<String> bad = List.of("0,0", "1,0,", "a,b");

        assertThrows(IllegalArgumentException.class, () -> service.compute(bad));
    }
}
