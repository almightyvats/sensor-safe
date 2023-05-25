package com.almightyvats.sensorsafe.sanity.util;

import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.service.ReadingService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class SanityCheckUtil {

    @Autowired
    private static ReadingService readingService;

    public static boolean isValueWithinRange(double value, SensorProperty sensorProperty) {
        return value >= sensorProperty.getMinValue() && value <= sensorProperty.getMaxValue();
    }

    public static List<Document> getReadingsBySensorIdAndTimeRange(String sensorId, Date from, Date to) {
        List<Document> readings =  readingService.getReadingsBySensorIdAndTimestampRange(sensorId, from, to);
        assert readings != null;
        return readings;
    }
}
