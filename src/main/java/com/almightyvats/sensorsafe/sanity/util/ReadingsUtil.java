package com.almightyvats.sensorsafe.sanity.util;

import org.bson.Document;

import java.util.Date;
import java.util.List;

public class ReadingsUtil {

    private static final Double DEFAULT_INVALID_VALUE = -999.0;
    public enum CalculateValueType {
        SUM, AVG, MIN, MAX, STD
    }

    public static double getCalculatedValue(List<Document> readings, CalculateValueType calculateValueType) {
        double result = DEFAULT_INVALID_VALUE;
        List<Double> readingValues = getValues(readings);
        switch (calculateValueType) {
            case SUM -> result = readingValues.stream().mapToDouble(Double::doubleValue).sum();
            case AVG -> result = readingValues.stream().mapToDouble(Double::doubleValue).average().orElse(DEFAULT_INVALID_VALUE);
            case MIN -> result = readingValues.stream().mapToDouble(Double::doubleValue).min().orElse(DEFAULT_INVALID_VALUE);
            case MAX -> result = readingValues.stream().mapToDouble(Double::doubleValue).max().orElse(DEFAULT_INVALID_VALUE);
            case STD -> result = Math.sqrt(readingValues.stream().mapToDouble(Double::doubleValue).average().orElse(DEFAULT_INVALID_VALUE));
        }
        return result;
    }

    public static double getLastValue(List<Document> readings) {
        return getValues(readings).get(readings.size() - 1);
    }

    // list of values for list of documents
    public static List<Double> getValues(List<Document> readings) {
        return readings.stream().map(document -> document.getDouble("value")).toList();
    }

    // date of min value
    public static Date getMinTimestamp(List<Document> readings) {
        return getTimestamp(readings, false);
    }

    // date of max value
    public static Date getMaxTimestamp(List<Document> readings) {
        return getTimestamp(readings, true);
    }

    private static Date getTimestamp(List<Document> readings, boolean isMax){
        return isMax ? readings.stream().max(ReadingsUtil::compareByValues).get().getDate("timestamp")
                : readings.stream().min(ReadingsUtil::compareByValues).get().getDate("timestamp");
    }

    private static int compareByValues(Document d1, Document d2) {
        return Double.compare(d1.getDouble("value"), d2.getDouble("value"));
    }
}
