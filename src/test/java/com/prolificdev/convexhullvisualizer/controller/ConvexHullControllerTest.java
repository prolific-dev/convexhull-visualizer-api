package com.prolificdev.convexhullvisualizer.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prolificdev.convexhullvisualizer.dto.request.ConvexHullRequest;
import com.prolificdev.convexhullvisualizer.core.result.ConvexHullResult;
import com.prolificdev.convexhullvisualizer.service.ConvexHullService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConvexHullController.class)
class ConvexHullControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConvexHullService convexHullService;

    @Test
    void compute2D_detail_returnsHull() throws Exception {
        List<String> input = List.of("0,0", "1,0", "0,1");
        ConvexHullRequest request = new ConvexHullRequest(input);

        ConvexHullResult<String> result = new ConvexHullResult<>(
                List.copyOf(input), // input
                List.of("0,0"),   // base
                List.of(),          // colinear
                List.of("0,0", "1,0"), // hull
                "2D",
                "dummy",
                1L,
                LocalDateTime.now());

        when(convexHullService.compute(anyList())).thenReturn((ConvexHullResult) result);

        mockMvc.perform(post("/api/v1/convexhull/compute/2d/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hull[0]").value("0,0"))
                .andExpect(jsonPath("$.hull[1]").value("1,0"));
    }

    @Test
    void compute2D_detail_returnsAllFields() throws Exception {
        List<String> input = List.of("0,0", "1,0", "0,1");
        ConvexHullRequest request = new ConvexHullRequest(input);

        ConvexHullResult<String> result = new ConvexHullResult<>(
                List.copyOf(input), // input
                List.of("0,0"),   // base
                List.of("1,0"),   // colinear
                List.of("0,0", "1,0"), // hull
                "2D",
                "dummy-algo",
                42L,
                LocalDateTime.of(2020, 1, 1, 0, 0));

        when(convexHullService.compute(anyList())).thenReturn((ConvexHullResult) result);

        mockMvc.perform(post("/api/v1/convexhull/compute/2d/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.input[0]").value("0,0"))
                .andExpect(jsonPath("$.base[0]").value("0,0"))
                .andExpect(jsonPath("$.colinear[0]").value("1,0"))
                .andExpect(jsonPath("$.hull[1]").value("1,0"))
                .andExpect(jsonPath("$.algorithm").value("dummy-algo"))
                .andExpect(jsonPath("$.computationTimeMs").value(42));
    }

    @Test
    void compute2D_nonDetail_returnsHullOnly() throws Exception {
        List<String> input = List.of("0,0", "1,0", "0,1");
        ConvexHullRequest request = new ConvexHullRequest(input);

        ConvexHullResult<String> result = new ConvexHullResult<>(
                List.copyOf(input),
                List.of("0,0"),
                List.of(),
                List.of("0,0", "1,0"),
                "2D",
                "algo",
                5L,
                LocalDateTime.now());

        when(convexHullService.compute(anyList())).thenReturn((ConvexHullResult) result);

        mockMvc.perform(post("/api/v1/convexhull/compute/2d")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hull").isArray())
                .andExpect(jsonPath("$.hull[0]").value("0,0"))
                .andExpect(jsonPath("$.hull[1]").value("1,0"));
    }

    @Test
    void compute3D_nonDetail_returnsHullOnly() throws Exception {
        List<String> input = List.of("0,0,0", "1,0,0", "0,1,0");
        ConvexHullRequest request = new ConvexHullRequest(input);

        ConvexHullResult<String> result = new ConvexHullResult<>(
                List.copyOf(input),
                List.of("0,0,0"),
                List.of(),
                List.of("0,0,0", "1,0,0"),
                "3D",
                "algo3d",
                10L,
                LocalDateTime.now());

        when(convexHullService.compute(anyList())).thenReturn((ConvexHullResult) result);

        mockMvc.perform(post("/api/v1/convexhull/compute/3d")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hull").isArray())
                .andExpect(jsonPath("$.hull[0]").value("0,0,0"))
                .andExpect(jsonPath("$.hull[1]").value("1,0,0"));
    }

    @Test
    void compute3D_detail_returnsAllFields() throws Exception {
        List<String> input = List.of("0,0,0", "1,0,0", "0,1,0");
        ConvexHullRequest request = new ConvexHullRequest(input);

        ConvexHullResult<String> result = new ConvexHullResult<>(
                List.copyOf(input), // input
                List.of("0,0,0"),   // base
                List.of("1,0,0"),   // colinear
                List.of("0,0,0", "1,0,0"), // hull
                "3D",
                "dummy-3d",
                123L,
                LocalDateTime.of(2021, 6, 15, 12, 0));

        when(convexHullService.compute(anyList())).thenReturn((ConvexHullResult) result);

        mockMvc.perform(post("/api/v1/convexhull/compute/3d/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.input[0]").value("0,0,0"))
                .andExpect(jsonPath("$.base[0]").value("0,0,0"))
                .andExpect(jsonPath("$.colinear[0]").value("1,0,0"))
                .andExpect(jsonPath("$.hull[1]").value("1,0,0"))
                .andExpect(jsonPath("$.algorithm").value("dummy-3d"))
                .andExpect(jsonPath("$.computationTimeMs").value(123));
    }
}