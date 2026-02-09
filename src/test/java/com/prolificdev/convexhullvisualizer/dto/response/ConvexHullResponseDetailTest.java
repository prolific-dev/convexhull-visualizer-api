package com.prolificdev.convexhullvisualizer.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ConvexHullResponseDetailTest {

    @Test
    public void fieldsAndEquality() {
        List<String> hull = Arrays.asList("0,0", "1,1");
        List<String> input = Arrays.asList("0,0", "1,1", "2,2");
        List<String> base = Arrays.asList("0,0", "1,1");
        List<String> colinear = Arrays.asList("2,2");

        ConvexHullResponseDetail d1 = new ConvexHullResponseDetail(hull, input, base, colinear, "GRAHAM", 123L, "ts");
        ConvexHullResponseDetail d2 = new ConvexHullResponseDetail(Arrays.asList("0,0", "1,1"), Arrays.asList("0,0", "1,1", "2,2"), Arrays.asList("0,0", "1,1"), Arrays.asList("2,2"), "GRAHAM", 123L, "ts");

        assertEquals(hull, d1.getHull());
        assertEquals(input, d1.getInput());
        assertEquals(base, d1.getBase());
        assertEquals(colinear, d1.getColinear());
        assertEquals("GRAHAM", d1.getAlgorithm());
        assertEquals(123L, d1.getComputationTimeMs());
        assertEquals("ts", d1.getTimestamp());

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }
}
