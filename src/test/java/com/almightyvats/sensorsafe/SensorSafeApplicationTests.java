package com.almightyvats.sensorsafe;

import com.almightyvats.sensorsafe.service.SensorService;
import com.almightyvats.sensorsafe.service.StationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SensorSafeApplicationTests {

	@Autowired
	private StationService stationService;

	@Autowired
	private SensorService sensorService;

	@Test
	void uninit() {

		if (sensorService.findAll() != null) {
			sensorService.deleteAll();
		}

		if (stationService.findAll() != null) {
			stationService.deleteAll();
		}
	}

}
