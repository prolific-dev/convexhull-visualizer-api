package com.prolificdev.convexhullvisualizer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import com.prolificdev.convexhullvisualizer.core.ConvexHullCalculator;
import com.prolificdev.convexhullvisualizer.core.geometry.Point3D;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

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

    @Test
    void parses3DPointsRemovingDuplicates() {
        ConvexHullCalculator calculator = mock(ConvexHullCalculator.class);
        when(calculator.compute(any())).thenReturn(new ConvexHullResult<>(List.of(), List.of(), List.of(), List.of(), "3D", "dummy", 0, null));

        var service = new ConvexHullService(calculator);

        List<String> raw = List.of("0,0,0", "1,0,0", "0,1,0", "0,0,0");

        service.compute(raw);

        ArgumentCaptor<List<?>> captor = ArgumentCaptor.forClass(List.class);
        verify(calculator).compute(captor.capture());

        List<?> deduped = captor.getValue();
        assertEquals(3, deduped.size(), "Duplicate 3D points must be removed before computation");
        assertTrue(deduped.stream().allMatch(Point3D.class::isInstance), "All parsed points must be 3D");
    }
}
