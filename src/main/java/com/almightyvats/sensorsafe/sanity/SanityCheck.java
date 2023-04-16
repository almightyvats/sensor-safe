package com.almightyvats.sensorsafe.sanity;

import com.almightyvats.sensorsafe.core.util.ReadingPayload;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SanityCheckType;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import com.almightyvats.sensorsafe.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SanityCheck {

    @Autowired
    private SensorService sensorService;

    public SanityCheckType check(ReadingPayload readingPayload) {
        Sensor sensor = sensorService.findByName(readingPayload.getSensorName());
        assert sensor != null;
        String sensorHardwareName = sensor.getUniqueHardwareName();
        SensorType sensorType = sensor.getType();
        Double value = readingPayload.getValue();
        Date timestamp = new Date(readingPayload.getTimestamp() * 1000);
        SensorProperty sensorProperty = sensor.getParameters();
        return getSanityCheckType(sensorHardwareName, timestamp, value, sensorType, sensorProperty);
    }

    private SanityCheckType getSanityCheckType(String sensorHardwareName, Date timestamp, Double value,
                                               SensorType sensorType, SensorProperty sensorProperty) {
        if (value.isNaN()) {
            return SanityCheckType.READING_NAN;
        }
        if(!SanityCheckUtil.isValueWithinRange(value, sensorProperty)) {
            if (value < sensorProperty.getMinValue()) {
                return SanityCheckType.READING_TOO_LOW;
            } else {
                return SanityCheckType.READING_TOO_HIGH;
            }
        }

        return SanityCheckType.NO_ERROR;
    }

}
