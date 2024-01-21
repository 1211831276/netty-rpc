package com.jay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jay
 * @create 2024/1/21
 **/
@RestController
@RequestMapping("/")
public class Main {
    @GetMapping("/alive")
    public String testAlive() {
        return "alive";
    }
}
