package com.almightyvats.sensorsafe.service;

import com.almightyvats.sensorsafe.core.TsDbManager;
import com.almightyvats.sensorsafe.core.util.HardwareNameUtil;
import com.almightyvats.sensorsafe.core.util.ReadingPayload;
import com.almightyvats.sensorsafe.model.Sensor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Document> getReadingsBySensorName(String sensorName) {
        log.debug("Request to get all Readings");
        Sensor sensor = sensorService.findByName(sensorName);
        if (sensor == null) {
            log.error("Sensor not found with name: {}", sensorName);
            return null;
        }
        return tsDbManager.getReadingsBySensor(sensor.getUniqueHardwareName());
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
