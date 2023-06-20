package com.almightyvats.sensorsafe.core.util;

import lombok.Data;

@Data
public class ReadingPayload {
    private String sensorName;
    private String stationMacAddress;
//    private String uniqueHardwareName; // remove it not needed
    private Double value;
    private Long timestamp;
}
