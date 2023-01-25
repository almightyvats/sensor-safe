package com.almightyvats.sensorsafe.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "sensor")
public class Sensor {
    @Id
    private String id;
    private String name;
    // TODO: Change the type to enum sensorType
    private String type;
    // TODO: Change the type to enum sensorProperty
    private List<String> parameters;
}
