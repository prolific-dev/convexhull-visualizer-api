package com.prolificdev.convexhullvisualizer.service;

import java.io.File;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prolificdev.convexhullvisualizer.core.geometry.Point2D;

@Service
public class FileService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<Point2D> readPointsFromFile(String filename) throws IOException {
        File file = new File("src/main/resources/data/" + filename);
        return List.of(objectMapper.readValue(file, Point2D[].class));
    }

    public void writePointsToFile(String filename, List<Point2D> points) throws IOException {
        File file = new File("src/main/resources/data/" + filename);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, points);
    }
}
