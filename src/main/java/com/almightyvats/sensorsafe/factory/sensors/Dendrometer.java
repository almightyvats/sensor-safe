package com.almightyvats.sensorsafe.factory.sensors;

import com.almightyvats.sensorsafe.factory.sensors.property.DendrometerProperty;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sensor")
public class Dendrometer extends Sensor {
    private final DendrometerProperty parameters;

    public Dendrometer(String name, String uniqueHardwareName, SensorType type, DendrometerProperty parameters) {
        super(name, uniqueHardwareName, type);
        this.parameters = parameters;
    }

    @Override
    public SensorProperty getParameters() {
        return parameters;
    }
}
