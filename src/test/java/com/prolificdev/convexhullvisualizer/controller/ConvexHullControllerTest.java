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
    void compute_returnsHull() throws Exception {
        // given
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

        // when / then
        mockMvc.perform(post("/api/v1/convexhull/compute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hull[0]").value("0,0"))
                .andExpect(jsonPath("$.hull[1]").value("1,0"));
    }

    @Test
    void computeFull_returnsAllFields() throws Exception {
        // given
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

        mockMvc.perform(post("/api/v1/convexhull/compute-full")
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
}
