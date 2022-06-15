package com.security.pki.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class TestController {

    @GetMapping(value = "/test")
    public ResponseEntity<?> user() {
        System.out.println("Controller");
        return ResponseEntity.ok("controller");
    }
}
