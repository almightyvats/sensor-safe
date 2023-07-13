package com.almightyvats.sensorsafe.factory.sensors;

import com.almightyvats.sensorsafe.factory.sensors.property.SoilWaterContentProperty;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sensor")
public class SoilWaterContent extends Sensor {
    private final SoilWaterContentProperty parameters;

    public SoilWaterContent(String name, String uniqueHardwareName, SensorType type, SoilWaterContentProperty parameters) {
        super(name, uniqueHardwareName, type);
        this.parameters = parameters;
    }

    @Override
    public SensorProperty getParameters() {
        return parameters;
    }
}
