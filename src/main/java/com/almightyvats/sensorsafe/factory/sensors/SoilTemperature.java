package com.almightyvats.sensorsafe.factory.sensors;

import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sensor")
public class SoilTemperature extends Sensor {
    public SoilTemperature(String name, String uniqueHardwareName, SensorType type, SensorProperty parameters) {
        super(name, uniqueHardwareName, type, parameters);
    }
    // Additional properties and methods
}
