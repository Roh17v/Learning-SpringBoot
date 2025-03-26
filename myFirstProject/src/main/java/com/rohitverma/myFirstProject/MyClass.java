package com.rohitverma.myFirstProject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyClass {

    @RequestMapping("hello")
    public String sayHello() {
        return "Hello";
    }
}
