package com.prolificdev.convexhullvisualizer.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ConvexHullResponseBaseTest {

    @Test
    public void hullEqualsAndHashCode() {
        List<String> hull = Arrays.asList("0,0", "1,1", "2,2");
        ConvexHullResponseBase b1 = new ConvexHullResponseBase(hull);
        ConvexHullResponseBase b2 = new ConvexHullResponseBase(Arrays.asList("0,0", "1,1", "2,2"));

        assertEquals(hull, b1.getHull());
        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }
}
