package com.almightyvats.sensorsafe.core.util;

import com.almightyvats.sensorsafe.model.custom.SensorType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class SensorPayload {
    private String name;
    private SensorType type;
    private Map<String, Object> properties;
}
