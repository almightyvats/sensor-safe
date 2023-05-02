package com.almightyvats.sensorsafe.controller;

import com.almightyvats.sensorsafe.core.util.ReadingPayload;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import com.almightyvats.sensorsafe.service.ReadingService;
import com.almightyvats.sensorsafe.service.SensorService;
import com.almightyvats.sensorsafe.service.StationService;
import com.almightyvats.sensorsafe.util.CSVMiemingSensorType;
import com.almightyvats.sensorsafe.util.CSVReader;
import com.almightyvats.sensorsafe.util.ModelUtil;
import com.almightyvats.sensorsafe.util.TestConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Map;

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
class ReadingControllerTest {

    @Autowired
    private StationService stationService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private ReadingService readingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    //@BeforeAll
    void setUp() {
        log.info("Setting up test data");
        stationService.save(ModelUtil.createStation(TestConstants.STATION_ID_1, TestConstants.STATION_NAME_1,
                TestConstants.STATION_MAC_ADDRESS_1, TestConstants.STATION_LOCATION_1));
        sensorService.save(TestConstants.STATION_ID_1, ModelUtil.createSensor(TestConstants.SENSOR_ID_1,
                CSVMiemingSensorType.Tair_C.getName(), SensorType.AIR_TEMPERATURE));
    }

    //@AfterAll
    void tearDown() throws JsonProcessingException {
        log.info("Deleting test data");
        if (sensorService.findAll().size() != 0) {
            sensorService.delete(TestConstants.SENSOR_ID_1);
        }
        if (stationService.findAll().size() != 0) {
            stationService.delete(TestConstants.STATION_ID_1);
        }
        if (readingService.getCount() != 0) {
            readingService.deleteAll();
        }
    }

    @Test
    @Order(1)
    void saveReading() throws Exception {
        log.info("Testing saveReading()");
        Map<Date, Double> miemingCSV = CSVReader.getValueWithTimestamp(TestConstants.DATA_FILE_PATH_MIEMING_SUBSET,
                CSVMiemingSensorType.Tair_C, 96, 0);
        for (Map.Entry<Date, Double> entry : miemingCSV.entrySet()) {
            ReadingPayload reading = ModelUtil.createReading(CSVMiemingSensorType.Tair_C.getName(),
                    TestConstants.STATION_MAC_ADDRESS_1, entry.getKey(), entry.getValue());
            mockMvc.perform(post("/v1/reading/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(reading)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is("Reading saved successfully")));
        }
    }

    @Test
    @Order(2)
    void getAllReadings() throws Exception {
        log.info("Testing getAllReadings()");
        mockMvc.perform(get("/v1/reading/all/" + CSVMiemingSensorType.Tair_C.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(96)));
    }
}