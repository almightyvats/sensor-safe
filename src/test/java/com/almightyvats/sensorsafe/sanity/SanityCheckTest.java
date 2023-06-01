package com.almightyvats.sensorsafe.sanity;

import com.almightyvats.sensorsafe.model.custom.SanityCheckCount;
import com.almightyvats.sensorsafe.model.custom.SanityCheckType;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import com.almightyvats.sensorsafe.service.ReadingService;
import com.almightyvats.sensorsafe.service.SensorService;
import com.almightyvats.sensorsafe.service.StationService;
import com.almightyvats.sensorsafe.util.ModelUtil;
import com.almightyvats.sensorsafe.util.TestConstants;
import com.almightyvats.sensorsafe.util.TestDataGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SanityCheckTest {

    @Autowired
    private StationService stationService;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private ReadingService readingService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    void setUp() {
        log.info("Setting up test data");
        stationService.save(ModelUtil.createStation(TestConstants.STATION_ID_1, TestConstants.STATION_NAME_1,
                TestConstants.STATION_MAC_ADDRESS_1, TestConstants.STATION_LOCATION_1, TestConstants.STATION_EMAIL_1));
        sensorService.save(TestConstants.STATION_ID_1, ModelUtil.createSensor(TestConstants.SENSOR_ID_1,
                TestConstants.SENSOR_NAME_1, SensorType.AIR_TEMPERATURE));
    }

//    @Test
    void deleteAll() {
        log.info("Deleting all data");
        readingService.deleteAll();
        sensorService.deleteAll();
        stationService.deleteAll();
    }

    @Test
    @Order(1)
    void testReadingOutOfBounds() {
        log.info("Testing reading out of bounds");
        Long[] timestamps = TestDataGeneratorUtil.generateTimestamps(100, 1);
        List<Double> values = TestDataGeneratorUtil.generateValueOutOfBounds(-50.0, 50.0, 100, 35);
        int i = 0;
        for (Double value : values) {
            readingService.save(ModelUtil.createReading(TestConstants.SENSOR_NAME_1, TestConstants.STATION_MAC_ADDRESS_1, timestamps[i++], value));
        }
        List<SanityCheckCount> sanityCheckCounts = readingService.getSanityCheckTypeCountBySensorId(TestConstants.SENSOR_ID_1);
        int sanityCheckValue = 0;
        for (SanityCheckCount sanityCheckCount : sanityCheckCounts) {
            if (sanityCheckCount.getSanityCheckType().equals(SanityCheckType.READING_TOO_HIGH) ||
                    sanityCheckCount.getSanityCheckType().equals(SanityCheckType.READING_TOO_LOW)) {
                sanityCheckValue += sanityCheckCount.getCount();
            }
        }
        Assertions.assertEquals(35, sanityCheckValue);
    }

    @Test
    @Order(2)
    void testReadingAboveRateOfChange() {
        readingService.deleteAll();
        log.info("Testing reading above rate of change");
        Long[] timestamps = TestDataGeneratorUtil.generateTimestamps(100, 1);
        List<Double> values = TestDataGeneratorUtil.generateValueOutOfBoundsOfMaxRateOfChange(-50.0,
                50.0, 100, 35, 0.5, 1);
        int i = 0;
        for (Double value : values) {
            readingService.save(ModelUtil.createReading(TestConstants.SENSOR_NAME_1, TestConstants.STATION_MAC_ADDRESS_1, timestamps[i++], value));
        }
        List<SanityCheckCount> sanityCheckCounts = readingService.getSanityCheckTypeCountBySensorId(TestConstants.SENSOR_ID_1);
        int sanityCheckValue = 0;
        for (SanityCheckCount sanityCheckCount : sanityCheckCounts) {
            if (sanityCheckCount.getSanityCheckType().equals(SanityCheckType.READING_ABOVE_RATE_OF_CHANGE)) {
                sanityCheckValue += sanityCheckCount.getCount();
            }
        }
        Assertions.assertEquals(35, sanityCheckValue);
    }
}
