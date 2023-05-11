package com.almightyvats.sensorsafe.util;

import com.almightyvats.sensorsafe.model.custom.SensorType;
import com.almightyvats.sensorsafe.service.SensorService;
import com.almightyvats.sensorsafe.service.StationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestDataUtil {

    @Autowired
    private StationService stationService;

    @Autowired
    private SensorService sensorService;

    @Test
    void setUp() {
        stationService.save(ModelUtil.createStation(TestConstants.STATION_ID_1, TestConstants.STATION_NAME_1,
                TestConstants.STATION_MAC_ADDRESS_1, TestConstants.STATION_LOCATION_1, TestConstants.STATION_EMAIL_1));
        sensorService.save(TestConstants.STATION_ID_1, ModelUtil.createSensor(TestConstants.SENSOR_ID_1,
                CSVMiemingSensorType.SWC_1_5cm_m3m_3.getName(), SensorType.SOIL_WATER_CONTENT));
        sensorService.save(TestConstants.STATION_ID_1, ModelUtil.createSensor(TestConstants.SENSOR_ID_2,
                CSVMiemingSensorType.SWC_1_10cm_m3m_3.getName(), SensorType.SOIL_WATER_CONTENT));
        sensorService.save(TestConstants.STATION_ID_1, ModelUtil.createSensor(TestConstants.SENSOR_ID_3,
                CSVMiemingSensorType.SWC_1_20cm_m3m_3.getName(), SensorType.SOIL_WATER_CONTENT));

        stationService.save(ModelUtil.createStation(TestConstants.STATION_ID_2, TestConstants.STATION_NAME_2,
                TestConstants.STATION_MAC_ADDRESS_2, TestConstants.STATION_LOCATION_2, TestConstants.STATION_EMAIL_1));
        sensorService.save(TestConstants.STATION_ID_2, ModelUtil.createSensor(TestConstants.SENSOR_ID_4,
                CSVMiemingSensorType.Dendrometer_1_micro_m.getName(), SensorType.DENDROMETER));
        sensorService.save(TestConstants.STATION_ID_2, ModelUtil.createSensor(TestConstants.SENSOR_ID_5,
                CSVMiemingSensorType.Dendrometer_2_micro_m.getName(), SensorType.DENDROMETER));
        sensorService.save(TestConstants.STATION_ID_2, ModelUtil.createSensor(TestConstants.SENSOR_ID_6,
                CSVMiemingSensorType.Dendrometer_3_micro_m.getName(), SensorType.DENDROMETER));
    }
}
