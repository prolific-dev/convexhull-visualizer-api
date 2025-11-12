package com.prolificdev.convexhullvisualizer.core.algorithm;

import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;

import java.util.List;

public interface ConvexHullAlgorithm<T> {
    ConvexHullResult<T> compute(List<T> points);
}
