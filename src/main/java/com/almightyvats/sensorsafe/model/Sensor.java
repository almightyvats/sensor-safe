package com.almightyvats.sensorsafe.model;

import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "sensor")
public class Sensor {
    @Id
    private String id;
    private String name;
    private String uniqueHardwareName;
    private SensorType type;
    private SensorProperty parameters;
}
