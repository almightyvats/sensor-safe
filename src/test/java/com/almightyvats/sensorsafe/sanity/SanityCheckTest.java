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

        @Test
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

    @Test
    @Order(3)
    void testReadingDuplicate() {
        readingService.deleteAll();
        log.info("Testing reading duplicate");
        Long[] timestamps = TestDataGeneratorUtil.generateTimestamps(15, 1);
        List<Double> values = TestDataGeneratorUtil.generateValueOutOfBoundsOfMaxRateOfChange(-50.0,
                50.0, 15, 0, 0.5, 1);
        int i = 0;
        for (Double value : values) {
            readingService.save(ModelUtil.createReading(TestConstants.SENSOR_NAME_1, TestConstants.STATION_MAC_ADDRESS_1, timestamps[i++], value));
        }
        for (int j = 0; j < 7; j++) {
            Double value = values.get(j);
            readingService.save(ModelUtil.createReading(TestConstants.SENSOR_NAME_1, TestConstants.STATION_MAC_ADDRESS_1, timestamps[j], value));
        }
        List<SanityCheckCount> sanityCheckCounts = readingService.getSanityCheckTypeCountBySensorId(TestConstants.SENSOR_ID_1);
        int sanityCheckValue = 0;
        for (SanityCheckCount sanityCheckCount : sanityCheckCounts) {
            if (sanityCheckCount.getSanityCheckType().equals(SanityCheckType.READING_DUPLICATE)) {
                sanityCheckValue += sanityCheckCount.getCount();
            }
        }
        Assertions.assertEquals(7, sanityCheckValue);
    }

    @Test
    @Order(4)
    void testReadingFrozen() {
        readingService.deleteAll();
        log.info("Testing reading frozen");
        Long[] timestamps = TestDataGeneratorUtil.generateTimestamps(10, 1);
        List<Double> values = TestDataGeneratorUtil.generateValueOutOfBoundsOfMaxRateOfChange(-50.0,
                50.0, 10, 0, 0.5, 1);

        for (int i = 0; i < 10; i++) {
            Double value = values.get(0);
            readingService.save(ModelUtil.createReading(TestConstants.SENSOR_NAME_1, TestConstants.STATION_MAC_ADDRESS_1, timestamps[i], value));
        }
        List<SanityCheckCount> sanityCheckCounts = readingService.getSanityCheckTypeCountBySensorId(TestConstants.SENSOR_ID_1);
        int sanityCheckValue = 0;
        for (SanityCheckCount sanityCheckCount : sanityCheckCounts) {
            if (sanityCheckCount.getSanityCheckType().equals(SanityCheckType.READING_INVALID_FROZEN_SENSOR)) {
                sanityCheckValue += sanityCheckCount.getCount();
            }
        }
        // Since the frozen time threshold is set for 5 hours, we expect 4 sanity check counts
        Assertions.assertEquals(4, sanityCheckValue);
    }

    @Test
    @Order(5)
    void testReadingGap() {
        readingService.deleteAll();
        log.info("Testing reading gap");
        Long[] timestamps = TestDataGeneratorUtil.generateTimestamps(10, 1);
        List<Double> values = TestDataGeneratorUtil.generateValueOutOfBoundsOfMaxRateOfChange(-50.0,
                50.0, 10, 0, 0.5, 1);

        for (int i = 0; i < 10; i++) {
            if (i <= 1 || i >= 6) {
                Double value = values.get(i);
                readingService.save(ModelUtil.createReading(TestConstants.SENSOR_NAME_1, TestConstants.STATION_MAC_ADDRESS_1, timestamps[i], value));
            }
        }
        List<SanityCheckCount> sanityCheckCounts = readingService.getSanityCheckTypeCountBySensorId(TestConstants.SENSOR_ID_1);
        int sanityCheckValue = 0;
        for (SanityCheckCount sanityCheckCount : sanityCheckCounts) {
            if (sanityCheckCount.getSanityCheckType().equals(SanityCheckType.READING_INVALID_GAP_TOO_BIG)) {
                sanityCheckValue += sanityCheckCount.getCount();
            }
        }
        Assertions.assertEquals(1, sanityCheckValue);
    }

    @Test
    @Order(6)
    void testReadingSpike() {
        readingService.deleteAll();
        log.info("Testing reading spike");
        Long[] timestamps = TestDataGeneratorUtil.generateTimestamps(12, 1);

        for (int i = 0; i < 12; i++) {
            double value = TestDataGeneratorUtil.incomingDataForSpikeDetection()[i];
            readingService.save(ModelUtil.createReading(TestConstants.SENSOR_NAME_1, TestConstants.STATION_MAC_ADDRESS_1, timestamps[i], value));
        }
        List<SanityCheckCount> sanityCheckCounts = readingService.getSanityCheckTypeCountBySensorId(TestConstants.SENSOR_ID_1);
        int sanityCheckValue = 0;
        for (SanityCheckCount sanityCheckCount : sanityCheckCounts) {
            if (sanityCheckCount.getSanityCheckType().equals(SanityCheckType.READING_INVALID_SPIKE)) {
                sanityCheckValue += sanityCheckCount.getCount();
            }
        }
        Assertions.assertEquals(2, sanityCheckValue);
    }
}
