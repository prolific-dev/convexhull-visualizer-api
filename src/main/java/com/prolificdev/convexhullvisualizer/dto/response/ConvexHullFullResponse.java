package com.prolificdev.convexhullvisualizer.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvexHullFullResponse {
    private List<String> input;
    private List<String> base;
    private List<String> colinear;
    private List<String> hull;

    private String algorithm;
    private long computationTimeMs;
    private String timestamp;
}
