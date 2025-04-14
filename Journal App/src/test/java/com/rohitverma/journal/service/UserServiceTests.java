package com.rohitverma.journal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserServiceTests {

    @ParameterizedTest
    @CsvSource({
            "4, 2, 2",
            "3, 1, 2",
            "9, 4, 5"
    })
    public void testAdd(int expected, int a, int b)
    {
        assertEquals(expected, a+b);
    }
}
