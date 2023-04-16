package com.almightyvats.sensorsafe.sanity;

import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SanityCheckUtil {

    @Autowired
    private ReadingService readingService;
    public static boolean isValueWithinRange(double value, SensorProperty sensorProperty) {
        return value >= sensorProperty.getMinValue() && value <= sensorProperty.getMaxValue();
    }
}
