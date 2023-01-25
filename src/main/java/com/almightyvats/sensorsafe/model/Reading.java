package com.almightyvats.sensorsafe.model;

import com.almightyvats.sensorsafe.model.util.SanityCheckType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.TimeSeries;

import java.util.Date;

@Data
@TimeSeries(collection = "reading", timeField = "timestamp")
public class Reading {
    @Id
    private String id;
    private String sensorName;
    private String uniqueHardwareName;
    private String value;
    private Date timestamp;
    // Not sure if we need the @DBRef annotation here
    private SanityCheckType sanityFlag;
}
