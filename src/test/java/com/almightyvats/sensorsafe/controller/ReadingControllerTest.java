package com.almightyvats.sensorsafe.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadingControllerTest {

    @BeforeAll
    void setUp() {
    }

    @AfterAll
    void tearDown() {
    }

    @Test
    void saveReading() {
    }

    @Test
    void getAllReadings() {
    }
}