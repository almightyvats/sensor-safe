package com.almightyvats.sensorsafe.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "station")
public class Station {
    @Id
    private String id;
    private String name;
    private String macAddress;
    private String location;
}
