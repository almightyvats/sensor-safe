package com.almightyvats.sensorsafe.sanity.util;

import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import com.almightyvats.sensorsafe.service.ReadingService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smile.math.MathEx;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Component
public class SanityCheckUtil {
    private static ReadingService readingService;
    SanityCheckUtil(ReadingService readingService) {
        SanityCheckUtil.readingService = readingService;
    }

    private static final double SPIKE_THRESHOLD = 3.5;
    private static final int GAP_THRESHOLD = 5; // hours
    private static final long A_DAY_IN_MILLISECONDS = 86399999L;

    public static boolean isValueWithinRange(double value, SensorProperty sensorProperty) {
        return value >= sensorProperty.getMinValue() && value <= sensorProperty.getMaxValue();
    }

    public static List<Document> getReadingsBySensorIdAndTimeRange(String sensorId, Date from, Date to) {
        List<Document> readings =  readingService.getReadingsBySensorIdAndTimestampRange(sensorId, from, to);
        assert readings != null;
        return readings;
    }

    public static long getNumberOfReadingsForSensor(String sensorId) {
        return readingService.getCountBySensorId(sensorId);
    }

    public static boolean isAlreadyInDatabase(String sensorId, double value, Date date) {
        Document query = new Document();
        query.append("timestamp", date);
        query.append("value", value);

        return readingService.ifDocumentExists(sensorId, query);
    }

    public static boolean isSensorFrozen(List<Document> readings, double value) {
        boolean isFrozen = readings.size() > 0;
        for (Document document : readings) {
            double databaseValue = document.getDouble("value");
            if (databaseValue != value) {
                isFrozen = false;
            }
        }
        return isFrozen;
    }

    public static boolean isSpike(List<Document> readings, double value) {
        List<Double> values = ReadingsUtil.getValues(readings);
        if (values.size() < 2) {
            return false;
        }
        double[] data = values.stream().mapToDouble(d -> d).toArray();
        double mean = MathEx.mean(data);
        double sd = MathEx.sd(data);
        double zScore = 0;
        if (sd != 0) {
            zScore = (value - mean) / sd;
        }
        return zScore > SPIKE_THRESHOLD;
    }

    public static double getRateOfChangePerHour(Document latestReading, double value, Date date) {
        double rateOfChangePerHour = 0;
        if (latestReading != null) {
            Date latestDate = latestReading.getDate("timestamp");
            double latestValue = latestReading.getDouble("value");
            double timeDifference = getTimeDifferenceInHours(date, latestDate);
            if (timeDifference != 0) {
                rateOfChangePerHour = (value - latestValue) / timeDifference;
            }
        }
        return rateOfChangePerHour;
    }

    public static boolean isGapTooBig(Date timestamp_current, Date timestamp_previous) {
        return getTimeDifferenceInHours(timestamp_current, timestamp_previous) >=  GAP_THRESHOLD;
    }

    // function to get 24 hours before a given date
    public static Date get24HoursBefore(Date requestedDate) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return new Date(requestedDate.getTime() - A_DAY_IN_MILLISECONDS);
    }


    // function for calculating the time difference between two timestamps in hours
    public static double getTimeDifferenceInHours(Date date1, Date date2) {
        return getTimeDifference(date1, date2) / 3600.0;
    }

    public static double getTimeDifference(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / 1000.0;
    }
}
