package com.prolificdev.convexhullvisualizer.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvexHullResponse {
    private List<String> hull;
}
