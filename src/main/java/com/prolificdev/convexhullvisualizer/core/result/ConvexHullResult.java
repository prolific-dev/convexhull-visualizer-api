package com.prolificdev.convexhullvisualizer.core.result;

import java.util.List;
import java.time.LocalDateTime;

public record ConvexHullResult<T>(
    List<T> input,
    List<T> base,
    List<T> colinear,
    List<T> hull,
    String dimension,
    String algorithm,
    long computationTimeMs,
    LocalDateTime timestamp
    ) {}
