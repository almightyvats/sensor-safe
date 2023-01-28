package com.almightyvats.sensorsafe.core.util;

import lombok.Data;

@Data
public class ReadingPayload {
    private String sensorName;
    private String stationMacAddress;
    private String uniqueHardwareName;
    private Double value;
    private Long timestamp;
}
