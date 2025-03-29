package com.rohitverma.journal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetHealth {

    @GetMapping("health-check")
    public String getHealth()
    {
        return "OK";
    }
}
