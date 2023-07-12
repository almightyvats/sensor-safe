package com.almightyvats.sensorsafe.factory.sensors;

import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sensor")
public class SolarRadiation extends Sensor {
    public SolarRadiation(String name, String uniqueHardwareName, SensorType type, SensorProperty parameters) {
        super(name, uniqueHardwareName, type, parameters);
    }
    // Additional properties and methods
    @Getter @Setter
    private double latitude;
    @Getter @Setter
    private double longitude;
}
