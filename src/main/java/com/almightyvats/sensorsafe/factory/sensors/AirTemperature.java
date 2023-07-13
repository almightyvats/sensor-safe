package com.almightyvats.sensorsafe.factory.sensors;

import com.almightyvats.sensorsafe.factory.sensors.property.AirTemperatureProperty;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sensor")
public class AirTemperature extends Sensor {
    private final AirTemperatureProperty parameters;

    public AirTemperature(String name, String uniqueHardwareName, SensorType type, AirTemperatureProperty parameters) {
        super(name, uniqueHardwareName, type);
        this.parameters = parameters;
    }

    @Override
    public SensorProperty getParameters() {
        return parameters;
    }
}
