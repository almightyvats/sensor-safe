package com.almightyvats.sensorsafe.factory.sensors;

import com.almightyvats.sensorsafe.factory.sensors.property.PrecipitationProperty;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sensor")
public class Precipitation extends Sensor {
    private final PrecipitationProperty parameters;

    public Precipitation(String name, String uniqueHardwareName, SensorType type, PrecipitationProperty parameters) {
        super(name, uniqueHardwareName, type);
        this.parameters = parameters;
    }

    @Override
    public SensorProperty getParameters() {
        return parameters;
    }
}
