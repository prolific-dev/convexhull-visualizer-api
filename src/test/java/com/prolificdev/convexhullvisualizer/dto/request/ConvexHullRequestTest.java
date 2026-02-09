package com.prolificdev.convexhullvisualizer.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ConvexHullRequestTest {

    @Test
    public void gettersSettersAndEquals() {
        List<String> input = Arrays.asList("0,0", "1,1", "2,2");
        ConvexHullRequest r1 = new ConvexHullRequest(input);
        ConvexHullRequest r2 = new ConvexHullRequest(Arrays.asList("0,0", "1,1", "2,2"));

        assertEquals(input, r1.getInput());
        assertEquals(r2, r1);

        r1.setInput(Arrays.asList());
        assertNotNull(r1.toString());
    }
}
