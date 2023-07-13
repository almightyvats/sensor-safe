package com.almightyvats.sensorsafe.controller;

import com.almightyvats.sensorsafe.core.util.SensorPayload;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.Station;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import com.almightyvats.sensorsafe.service.SensorService;
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
class SensorControllerTest {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private StationService stationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeAll
    void setUp() {
        log.info("Setting up test data");
        Station station = ModelUtil.createStation(TestConstants.STATION_ID_1, TestConstants.STATION_NAME_1,
                TestConstants.STATION_MAC_ADDRESS_1, TestConstants.STATION_LOCATION_1, TestConstants.STATION_EMAIL_1);
        stationService.save(station);

        SensorPayload sensor = ModelUtil.createSensor(TestConstants.SENSOR_ID_1, TestConstants.SENSOR_NAME_1,
                SensorType.AIR_TEMPERATURE);
        sensorService.save(TestConstants.STATION_ID_1, sensor);
    }

    @AfterAll
    void tearDown() {
        log.info("Deleting test data");
        if (sensorService.findAll().size() != 0) {
            sensorService.deleteAll();
        }
        if (stationService.findAll().size() != 0) {
            stationService.deleteAll();
        }
    }

    @Test
    @Order(1)
    void getAllSensors() throws Exception {
        log.info("Testing getAllSensors");
        mockMvc.perform(get("/v1/sensor/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(TestConstants.SENSOR_ID_1)));
    }

    @Test
    @Order(2)
    void getSensorById() throws Exception {
        log.info("Testing getSensorById");
        mockMvc.perform(get("/v1/sensor/{id}", TestConstants.SENSOR_ID_1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TestConstants.SENSOR_ID_1)));
    }

    @Test
    @Order(3)
    void saveSensor() throws Exception {
        log.info("Testing saveSensor");
        SensorPayload sensor = ModelUtil.createSensor(TestConstants.SENSOR_ID_2, TestConstants.SENSOR_NAME_2,
                SensorType.RELATIVE_HUMIDITY);

        mockMvc.perform(post("/v1/sensor/add/{id}", TestConstants.STATION_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sensor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TestConstants.SENSOR_ID_2)));
    }

    @Test
    @Order(4)
    void updateSensor() throws Exception {
        log.info("Testing updateSensor");
        Sensor sensor = sensorService.findById(TestConstants.SENSOR_ID_1);
        sensor.setName(TestConstants.SENSOR_NAME_3);

        mockMvc.perform(put("/v1/sensor/update/" + TestConstants.STATION_ID_1 + "/" +
                        TestConstants.SENSOR_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sensor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(TestConstants.SENSOR_NAME_3)));
    }

    @Test
    @Order(5)
    void deleteSensor() throws Exception {
        log.info("Testing deleteSensor");
        mockMvc.perform(delete("/v1/sensor/delete/{id}", TestConstants.SENSOR_ID_1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
