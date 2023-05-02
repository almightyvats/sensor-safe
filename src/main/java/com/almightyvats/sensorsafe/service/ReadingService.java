package com.almightyvats.sensorsafe.service;

import com.almightyvats.sensorsafe.core.TsDbManager;
import com.almightyvats.sensorsafe.core.util.CSVManager;
import com.almightyvats.sensorsafe.core.util.HardwareNameUtil;
import com.almightyvats.sensorsafe.core.util.ReadingPayload;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SanityCheckCount;
import com.almightyvats.sensorsafe.model.custom.SanityCheckType;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReadingService {

    @Autowired
    private TsDbManager tsDbManager;

    @Autowired
    private SensorService sensorService;

    /**
     * Save a reading.
     *
     * @param reading the entity to save.
     * @return the persisted entity.
     */
    public void save(ReadingPayload reading) {
        try {
            log.debug("Request to save Reading : {}", reading);
            //TODO: Sanity check shall be applied here
            Document document = getDocument(reading);
            tsDbManager.insert(document);
        } catch (Exception e) {
            log.error("Error while saving reading", e);
        }
    }

    private Document getDocument(ReadingPayload reading) {
        Document document = new Document();
        document.append("sensorName", reading.getSensorName());
        document.append("stationMac", reading.getStationMacAddress());
        document.append("uniqueHardwareName", HardwareNameUtil.getUniqueHardwareName(reading.getSensorName(),
                reading.getStationMacAddress()));
        document.append("value", reading.getValue());
        document.append("timestamp", new Date(reading.getTimestamp() * 1000));
        return document;
    }

    /**
     * Get the readings with the given sensor name.
     *
     * @return the list of entities.
     */
    public List<Document> getReadingsBySensorId(String id) {
        log.debug("Request to get all Readings");
        Sensor sensor = sensorService.findById(id);
        if (sensor == null) {
            log.error("Sensor not found with id: {}", id);
            return null;
        }
        return tsDbManager.getReadingsBySensor(sensor.getUniqueHardwareName());
    }

    /**
     * Get the filtered list of SanityCheckType with count of each type for the given sensor name.
     *
     * @return the list of SanityCheckType with count of each type.
     */
    public List<SanityCheckCount> getSanityCheckTypeCountBySensorId(String id) {
        log.debug("Request to get SanityCheckType count for sensor: {}", id);
        Sensor sensor = sensorService.findById(id);
        if (sensor == null) {
            log.error("Sensor not found with id: {}", id);
            return null;
        }

        List<SanityCheckCount> sanityCheckCountList = new ArrayList<>();
         for (SanityCheckType sanityCheckType : SanityCheckType.values()) {
             sanityCheckCountList.add(new SanityCheckCount(sanityCheckType,
                     tsDbManager.getSanityCheckTypeCountBySensor(sensor.getUniqueHardwareName(), sanityCheckType)));
         }
         return sanityCheckCountList;
    }

    /**
     * Download csv file for the given sensor name.
     * @return the csv byte[]
     */
    public byte[] downloadCsv(String id) {
        log.debug("Request to download csv for sensor: {}", id);
        Sensor sensor = sensorService.findById(id);
        if (sensor == null) {
            log.error("Sensor not found with id: {}", id);
            return null;
        }
        return CSVManager.createCSVFile(tsDbManager.getReadingsBySensor(sensor.getUniqueHardwareName()));
    }

    /**
     * FOR TESTING PURPOSES ONLY
     * Get the count of all the readings.
     * @return the count of all the readings.
     */
    public long getCount() {
        return tsDbManager.getNumberOfDocuments();
    }

    /**
     * FOR TESTING PURPOSES ONLY
     * Delete all the readings.
     */
    public void deleteAll() {
        log.debug("Request to delete all Readings");
        tsDbManager.deleteAllDocuments();
    }
}
