package com.almightyvats.sensorsafe.controller;

import com.almightyvats.sensorsafe.model.Station;
import com.almightyvats.sensorsafe.service.StationService;
import com.almightyvats.sensorsafe.util.ModelUtil;
import com.almightyvats.sensorsafe.util.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StationControllerTest {

    @Autowired
    private StationService stationService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    void setUp() {
        log.info("Setting up test data");
        Station station = ModelUtil.createStation(TestConstants.STATION_ID_1, TestConstants.STATION_NAME_1,
                TestConstants.STATION_MAC_ADDRESS_1, TestConstants.STATION_LOCATION_1, TestConstants.STATION_EMAIL_1);
        stationService.save(station);
    }

    @AfterAll
    void tearDown() {
        log.info("Deleting test data");
        if (stationService.findAll().size() != 0) {
            stationService.deleteAll();
        }
    }

    @Test
    @Order(1)
    void getAllStations() throws Exception {
        log.info("Testing getAllStations");
        mockMvc.perform(get("/v1/station/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(TestConstants.STATION_ID_1)));
    }

    @Test
    @Order(2)
    void getStationById() throws Exception {
        log.info("Testing getStationById");
        mockMvc.perform(get("/v1/station/{id}", TestConstants.STATION_ID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TestConstants.STATION_ID_1)));
    }

    @Test
    @Order(3)
    void saveStation() throws Exception {
        log.info("Testing saveStation");
        Station station = ModelUtil.createStation(TestConstants.STATION_ID_2, TestConstants.STATION_NAME_2,
                TestConstants.STATION_MAC_ADDRESS_2, TestConstants.STATION_LOCATION_2, TestConstants.STATION_EMAIL_1);
        String json = mapper.writeValueAsString(station);
        mockMvc.perform(post("/v1/station/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TestConstants.STATION_ID_2)));
    }

    @Test
    @Order(4)
    void updateStation() throws Exception {
        log.info("Testing updateStation");
        Station station = stationService.findById(TestConstants.STATION_ID_1);
        station.setName(TestConstants.STATION_NAME_3);
        mockMvc.perform(put("/v1/station/update/{id}", TestConstants.STATION_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(station)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(TestConstants.STATION_NAME_3)));
    }

    @Test
    @Order(5)
    void deleteStation() throws Exception {
        log.info("Testing deleteStation");
        mockMvc.perform(delete("/v1/station/delete/{id}", TestConstants.STATION_ID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}