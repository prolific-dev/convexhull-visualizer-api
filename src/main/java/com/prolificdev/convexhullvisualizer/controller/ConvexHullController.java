package com.prolificdev.convexhullvisualizer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolificdev.convexhullvisualizer.dto.request.ConvexHullRequest;
import com.prolificdev.convexhullvisualizer.dto.response.ConvexHullFullResponse;
import com.prolificdev.convexhullvisualizer.dto.response.ConvexHullResponse;
import com.prolificdev.convexhullvisualizer.service.ConvexHullService;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/convexhull")
@RequiredArgsConstructor
public class ConvexHullController {
    private final ConvexHullService convexHullService;

    @PostMapping("/2d/compute/hull")
    public ConvexHullResponse compute2D(@RequestBody ConvexHullRequest request) {

        ConvexHullResult<?> result;
        ConvexHullResponse response = new ConvexHullResponse();

        result = convexHullService.compute(request.getInput());

        response.setHull(result.hull().stream().map(Object::toString).toList());

        return response;
    }
    
    @PostMapping("/3d/compute/hull")
    public ConvexHullResponse compute3D(@RequestBody ConvexHullRequest request) {

        ConvexHullResult<?> result;
        ConvexHullResponse response = new ConvexHullResponse();

        result = convexHullService.compute(request.getInput());

        response.setHull(result.hull().stream().map(Object::toString).toList());

        return response;
    }

    @PostMapping("/2d/compute/full")
    public ConvexHullFullResponse computeFull2D(@RequestBody ConvexHullRequest request) {
        ConvexHullResult<?> result;
        ConvexHullFullResponse response = new ConvexHullFullResponse();

        result = convexHullService.compute(request.getInput());

        response.setInput(request.getInput().stream().map(Object::toString).toList());
        response.setBase(result.base().stream().map(Object::toString).toList());
        response.setColinear(result.colinear().stream().map(Object::toString).toList());
        response.setHull(result.hull().stream().map(Object::toString).toList());
        response.setAlgorithm(result.algorithm());
        response.setComputationTimeMs(result.computationTimeMs());
        response.setTimestamp(result.timestamp().toString());
        
        return response;
    }

    @PostMapping("/3d/compute/full")
    public ConvexHullFullResponse computeFull3D(@RequestBody ConvexHullRequest request) {
        ConvexHullResult<?> result;
        ConvexHullFullResponse response = new ConvexHullFullResponse();

        result = convexHullService.compute(request.getInput());

        response.setInput(request.getInput().stream().map(Object::toString).toList());
        response.setBase(result.base().stream().map(Object::toString).toList());
        response.setColinear(result.colinear().stream().map(Object::toString).toList());
        response.setHull(result.hull().stream().map(Object::toString).toList());
        response.setAlgorithm(result.algorithm());
        response.setComputationTimeMs(result.computationTimeMs());
        response.setTimestamp(result.timestamp().toString());
        
        return response;
    }
}
