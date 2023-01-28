package com.almightyvats.sensorsafe.model;

import com.almightyvats.sensorsafe.model.custom.SanityCheckType;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
    private String stationMacAddress;
    private String uniqueHardwareName;
    private Double value;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    // Not sure if we need the @DBRef annotation here
    private SanityCheckType sanityFlag;
}
