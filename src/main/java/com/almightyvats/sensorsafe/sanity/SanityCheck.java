package com.almightyvats.sensorsafe.sanity;

import com.almightyvats.sensorsafe.core.util.ReadingPayload;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SanityCheckType;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import com.almightyvats.sensorsafe.sanity.util.ReadingsForSanity;
import com.almightyvats.sensorsafe.sanity.util.SanityCheckUtil;
import com.almightyvats.sensorsafe.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SanityCheck {

    @Autowired
    private SensorService sensorService;

    public List<SanityCheckType> check(ReadingPayload readingPayload) {
        Sensor sensor = sensorService.findByName(readingPayload.getSensorName());
        assert sensor != null;
        String sensorHardwareName = sensor.getUniqueHardwareName();
        SensorType sensorType = sensor.getType();
        Double value = readingPayload.getValue();
        Date timestamp = new Date(readingPayload.getTimestamp() * 1000);
        SensorProperty sensorProperty = sensor.getParameters();
        return getSanityCheckType(sensor.getId(), sensorHardwareName, timestamp, value, sensorType, sensorProperty);
    }

    private List<SanityCheckType> getSanityCheckType(String sensorId, String sensorHardwareName, Date timestamp, Double value,
                                               SensorType sensorType, SensorProperty sensorProperty) {
        List<SanityCheckType> sanityCheckTypeList = new ArrayList<>();
        if (value.isNaN()) {
            sanityCheckTypeList.add(SanityCheckType.READING_NAN);
            return sanityCheckTypeList;
        }
        if(!SanityCheckUtil.isValueWithinRange(value, sensorProperty)) {
            if (value < sensorProperty.getMinValue()) {
                sanityCheckTypeList.add(SanityCheckType.READING_TOO_LOW);
            } else {
                sanityCheckTypeList.add(SanityCheckType.READING_TOO_HIGH);
            }
        }
        if (SanityCheckUtil.getNumberOfReadingsForSensor(sensorId) > 0) {
            if (SanityCheckUtil.isAlreadyInDatabase(sensorId, value, timestamp)) {
                sanityCheckTypeList.add(SanityCheckType.READING_DUPLICATE);
            } else {
                Date from = SanityCheckUtil.get24HoursBefore(timestamp);
                ReadingsForSanity readingsForSanity = new ReadingsForSanity(sensorId, from, timestamp);
                if (readingsForSanity.getNumberOfReadingsIn24Hours() > 0) {
                    from = new Date(timestamp.getTime() - sensorProperty.getMaxFrozenTimeInSeconds() * 1000L);
                    if (SanityCheckUtil.isSensorFrozen(readingsForSanity.getReadingsBetweenTimestamps(from), value)) {
                        sanityCheckTypeList.add(SanityCheckType.READING_INVALID_FROZEN_SENSOR);
                    }
                    if (SanityCheckUtil.isGapTooBig(timestamp, readingsForSanity.getDateFromLastReading())) {
                        sanityCheckTypeList.add(SanityCheckType.READING_INVALID_GAP_TOO_BIG);
                    }
                    if (readingsForSanity.getNumberOfReadingsIn24Hours() > 4) {
                        if (SanityCheckUtil.isSpike(readingsForSanity.getReadingsInSpikeWindow(), value)) {
                            sanityCheckTypeList.add(SanityCheckType.READING_INVALID_SPIKE);
                        }
                    }
                    if (SanityCheckUtil.getRateOfChangePerHour(readingsForSanity.getLastReading(), value, timestamp) >
                            sensorProperty.getMaxRateOfChange()) {
                        sanityCheckTypeList.add(SanityCheckType.READING_ABOVE_RATE_OF_CHANGE);
                    }
                }
            }
        }

        return sanityCheckTypeList;
    }

}
