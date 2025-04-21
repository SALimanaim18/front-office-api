package com.example.frontofficeapi.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("SangConnect/demo-controller")
public class DemoController {
    @GetMapping
    public ResponseEntity<String> SayHello() {
        return ResponseEntity.ok("Hello World");
    }

}
