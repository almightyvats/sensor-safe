package com.almightyvats.sensorsafe.factory.sensors;

import com.almightyvats.sensorsafe.factory.sensors.property.SoilTemperatureProperty;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sensor")
public class SoilTemperature extends Sensor {
    private final SoilTemperatureProperty parameters;

    public SoilTemperature(String name, String uniqueHardwareName, SensorType type, SoilTemperatureProperty parameters) {
        super(name, uniqueHardwareName, type);
        this.parameters = parameters;
    }

    @Override
    public SensorProperty getParameters() {
        return parameters;
    }
}
