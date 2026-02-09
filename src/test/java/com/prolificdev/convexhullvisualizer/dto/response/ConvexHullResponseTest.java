package com.prolificdev.convexhullvisualizer.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ConvexHullResponseTest {

    @Test
    public void abstractClassConstructorsAndSetters() {
        List<String> hull = Arrays.asList("0,0", "1,1");

        ConvexHullResponse r1 = new ConvexHullResponse(hull) {};
        assertEquals(hull, r1.getHull());

        ConvexHullResponse r2 = new ConvexHullResponse() {};
        r2.setHull(hull);
        assertEquals(hull, r2.getHull());

        assertNotNull(r1.toString());
    }
}
