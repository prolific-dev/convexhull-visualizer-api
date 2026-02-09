package com.prolificdev.convexhullvisualizer.dto.response;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConvexHullResponseBase extends ConvexHullResponse {
    public ConvexHullResponseBase(List<String> hull) {
        super(hull);
    }
}