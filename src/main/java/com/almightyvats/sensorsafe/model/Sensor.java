package com.almightyvats.sensorsafe.model;

import com.almightyvats.sensorsafe.factory.sensors.property.AirTemperatureProperty;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "sensor")
public abstract class Sensor {
    @Id
    private String id;
    private String name;
    private String uniqueHardwareName;
    private SensorType type;

    public Sensor(String name, String uniqueHardwareName, SensorType type) {
        this.name = name;
        this.uniqueHardwareName = uniqueHardwareName;
        this.type = type;
    }

    public abstract SensorProperty getParameters();
}
