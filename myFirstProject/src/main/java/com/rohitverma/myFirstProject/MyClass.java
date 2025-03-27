package com.rohitverma.myFirstProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyClass {

    @Autowired
    public Car car;

    @RequestMapping("hello")
    public String sayHello() {
        return car.start();
    }
}
