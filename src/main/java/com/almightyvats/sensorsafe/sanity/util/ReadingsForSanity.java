package com.almightyvats.sensorsafe.sanity.util;

import com.almightyvats.sensorsafe.model.custom.SanityCheckType;
import org.bson.Document;

import java.util.*;

public class ReadingsForSanity {
    private final String sensorId;
    private final Date from;
    private final Date to;
    private final List<Document> readings;
    private static final int SPIKE_WINDOW_SIZE = 5;

    public ReadingsForSanity(String sensorId, Date from, Date to) {
        this.sensorId = sensorId;
        this.from = from;
        this.to = to;
        this.readings = setReadingsFor24Hours();
    }

    private List<Document> setReadingsFor24Hours() {
        return SanityCheckUtil.getReadingsBySensorIdAndTimeRange(sensorId, from, to);
    }

    public Document getLastReading() {
        return readings.get(readings.size() - 1);
    }

    public Date getDateFromLastReading() {
        return getLastReading().getDate("timestamp");
    }

    public List<Document> getReadingsInSpikeWindow() {
        // get the last SPIKE_WINDOW_SIZE readings
        List<Document> subsetReadings = new ArrayList<>(this.readings);
        int size = subsetReadings.size();
        if (size > SPIKE_WINDOW_SIZE) {
            // only get the last SPIKE_WINDOW_SIZE readings
            subsetReadings = subsetReadings.subList(size - SPIKE_WINDOW_SIZE, size);
            // check if any of the readings is a spike if the errorStatus array in the reading contains INVALID_READING_VALUE_SPIKE
            subsetReadings.removeIf(document -> document.get("errorStatus", List.class)
                    .contains(SanityCheckType.READING_INVALID_SPIKE.name()));
        }
        return subsetReadings;
    }

    public List<Document> getReadingsBetweenTimestamps(Date from) {
        // filter readings between from and to within the local readings list
        List<Document> subsetReadings = new ArrayList<>(this.readings);
        subsetReadings.removeIf(document -> document.getDate("timestamp").before(from));
        subsetReadings.removeIf(document -> document.getDate("timestamp").after(to));
        return subsetReadings;
    }

    public List<Double> getValuesFromReadings(List<Document> readings) {
        return ReadingsUtil.getValues(readings);
    }

    public List<Document> getReadingsFor24Hours() {
        return readings;
    }

    public long getNumberOfReadingsIn24Hours() {
        return this.readings.size();
    }
}
