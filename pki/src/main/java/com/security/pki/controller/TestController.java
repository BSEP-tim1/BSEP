package com.security.pki.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class TestController {

    @RequestMapping(method = RequestMethod.GET, value = "/test")
    public ResponseEntity<?> user() {
        System.out.println("Controller");
        return ResponseEntity.ok("controller");
    }
}
