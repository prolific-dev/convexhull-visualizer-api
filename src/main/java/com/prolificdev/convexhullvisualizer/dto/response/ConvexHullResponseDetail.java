package com.prolificdev.convexhullvisualizer.dto.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConvexHullResponseDetail extends ConvexHullResponse {
    private List<String> input;
    private List<String> base;
    private List<String> colinear;

    private String algorithm;
    private long computationTimeMs;
    private String timestamp;

    public ConvexHullResponseDetail(List<String> hull,
                                    List<String> input,
                                    List<String> base,
                                    List<String> colinear,
                                    String algorithm,
                                    long computationTimeMs,
                                    String timestamp) {
        super(hull);
        this.input = input;
        this.base = base;
        this.colinear = colinear;
        this.algorithm = algorithm;
        this.computationTimeMs = computationTimeMs;
        this.timestamp = timestamp;
    }
}
