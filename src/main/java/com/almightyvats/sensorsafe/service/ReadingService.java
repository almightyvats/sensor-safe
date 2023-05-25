package com.almightyvats.sensorsafe.service;

import com.almightyvats.sensorsafe.core.TsDbManager;
import com.almightyvats.sensorsafe.core.util.CSVManager;
import com.almightyvats.sensorsafe.core.util.HardwareNameUtil;
import com.almightyvats.sensorsafe.core.util.ReadingPayload;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.Station;
import com.almightyvats.sensorsafe.model.custom.SanityCheckCount;
import com.almightyvats.sensorsafe.model.custom.SanityCheckType;
import com.almightyvats.sensorsafe.sanity.SanityCheck;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ReadingService {

    @Autowired
    private TsDbManager tsDbManager;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private StationService stationService;

    @Autowired
    private SanityCheck sanityCheck;

    @Autowired
    private EmailService emailService;

    /**
     * Save a reading.
     *
     * @param reading the entity to save.
     */
    public void save(ReadingPayload reading) {
        try {
            log.debug("Request to save Reading : {}", reading);
            List<SanityCheckType> sanityCheckTypes = new ArrayList<>(sanityCheck.check(reading));
            Sensor sensor = sensorService.findByName(reading.getSensorName());
            if (sensor == null) {
                log.error("Sensor not found with name: {}", reading.getSensorName());
                return;
            }
            if (!sensor.getParameters().isEnable()) {
                log.debug("Sanity check is disabled for sensor: {}", sensor.getName());
                return;
            }
            if (!sanityCheckTypes.contains(SanityCheckType.NO_ERROR)) {
                String stationId = stationService.findStationIdBySensorId(sensor.getId());
                Station station = stationService.findById(stationId);
                log.error("Station not found with sensor id: {}", sensor.getId());
                String email = stationService.findEmailByStationId(stationId);
                log.error("Email not found with station id: {}", stationId);
                emailService.sendEmail(email, sensor.getName(), station.getName(), new Date(reading.getTimestamp() * 1000L),
                        sanityCheckTypes);
            }
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
     * Get readings with sensorId and timestamp range.
     *
     * @return the list of entities.
     */
    public List<Document> getReadingsBySensorIdAndTimestampRange(String id, Date from, Date to) {
        log.debug("Request to get all Readings");
        Sensor sensor = sensorService.findById(id);
        if (sensor == null) {
            log.error("Sensor not found with id: {}", id);
            return null;
        }
        return tsDbManager.getReadingsBySensorAndTimeRange(sensor.getUniqueHardwareName(), from, to);
    }

    /**
     * Check if document exists in the database
     *
     * @return boolean
     */
    public boolean ifDocumentExists(String sensorId, Document query) {
        String uniqueHardwareName = Objects.requireNonNull(sensorService.findById(sensorId))
                .getUniqueHardwareName();
        query.append("uniqueHardwareName", uniqueHardwareName);
        return tsDbManager.checkIfDocumentExists(query);
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
     * Get the count of all the readings with sensorId.
     * @return the count.
     */
    public long getCountBySensorId(String id) {
        log.debug("Request to get count of all Readings");
        Sensor sensor = sensorService.findById(id);
        if (sensor == null) {
            log.error("Sensor not found with id: {}", id);
            return 0;
        }
        return tsDbManager.getNumberOfDocumentsBySensor(sensor.getUniqueHardwareName());
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
