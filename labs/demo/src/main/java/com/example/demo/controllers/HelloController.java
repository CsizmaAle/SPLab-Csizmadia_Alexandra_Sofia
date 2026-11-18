package com.example.demo.controllers;

import com.example.demo.difexamples.ClientComponent;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record HelloController(ClientComponent clientComponent, ClientComponent otherClientComponent) {

    @GetMapping("/")
    public String hello() {
        return "Hello from ClientComponent = " + clientComponent;
        }
}
