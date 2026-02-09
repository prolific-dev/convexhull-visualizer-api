package com.prolificdev.convexhullvisualizer.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleIOExceptionReturnsInternalServerError() {
        IOException source = new IOException("disk failure");

        ResponseEntity<String> response = handler.handleIOException(source);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(
                "An internal error occurred while processing the file: " + source.getMessage(),
                response.getBody()
        );
    }

    @Test
    void handleIllegalArgumentExceptionReturnsBadRequest() {
        IllegalArgumentException source = new IllegalArgumentException("Missing required parameter");

        ResponseEntity<String> response = handler.handleIllegalArgumentException(source);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input: " + source.getMessage(), response.getBody());
    }
}
