package com.almightyvats.sensorsafe.service;

import com.almightyvats.sensorsafe.core.util.HardwareNameUtil;
import com.almightyvats.sensorsafe.core.util.SensorPayload;
import com.almightyvats.sensorsafe.factory.SensorFactory;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.Station;
import com.almightyvats.sensorsafe.repository.SensorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private StationService stationService;

    /**
     * Get all the sensors.
     *
     * @return the list of entities.
     */
    public List<Sensor> findAll() {
        log.debug("Request to get all Sensors");
        return sensorRepository.findAll();
    }

    /**
     * Get one sensor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Sensor findById(String id) {
        log.debug("Request to get Sensor : {}", id);
        return sensorRepository.findById(id).orElse(null);
    }

    /**
     * Get one sensor by name.
     *
     * @param name the name of the entity.
     * @return the entity.
     */
    public Sensor findByName(String name) {
        log.debug("Request to get Sensor : {}", name);
        return sensorRepository.findByName(name).orElse(null);
    }

    /**
     * Delete the sensor by id.
     *
     * @param id the id of the entity.
     */
    public String delete(String id) {
        log.debug("Request to delete Sensor : {}", id);
        sensorRepository.deleteById(id);
        return id;
    }

    /**
     * Save a sensor.
     *
     * @param stationId     the id of the station to which the sensor belongs.
     * @param sensorPayload the entity to save.
     * @return the persisted entity.
     */
    public Sensor save(String stationId, SensorPayload sensorPayload) {
        log.debug("Request to save Sensor : {}", sensorPayload);
        Station station = stationService.findById(stationId);
        if (station == null) {
            log.error("Station with id {} does not exist", stationId);
            return null;
        }
        String uniqueHardwareName = HardwareNameUtil.getUniqueHardwareName(sensorPayload.getName(),
                station.getMacAddress());

        if (sensorRepository.existsByName(sensorPayload.getName())) {
            log.error("Sensor with name {} already exists", sensorPayload.getName());
            return null;
        }

        Sensor sensorToSave = SensorFactory.createSensor(sensorPayload.getName(), uniqueHardwareName,
                sensorPayload.getType(), sensorPayload.getProperties());

        assert sensorToSave != null;
        Sensor savedSensor = sensorRepository.save(sensorToSave);
        stationService.addSensor(station.getId(), savedSensor.getId());
        return savedSensor;
    }

    /**
     * Update a sensor.
     *
     * @param sensorToUpdate the entity to update.
     * @return the persisted entity.
     */
    public Sensor update(String stationId, String sensorToUpdateId, SensorPayload sensorToUpdate) {
        log.debug("Request to update Sensor : {}", sensorToUpdate);
        Sensor sensor = sensorRepository.findById(sensorToUpdateId).orElse(null);
        if (sensor == null) {
            log.error("Sensor with id {} does not exist", sensorToUpdateId);
            return null;
        }
        Station station = stationService.findById(stationId);
        if (station == null) {
            log.error("Station with id {} does not exist", stationId);
            return null;
        }
        sensor.setName(sensorToUpdate.getName());
        sensor.setUniqueHardwareName(HardwareNameUtil.getUniqueHardwareName(sensorToUpdate.getName(),
                station.getMacAddress()));
        sensor.setType(sensorToUpdate.getType());
        return sensorRepository.save(sensor);
    }

    /**
     * FOR TESTING PURPOSES ONLY
     * <p>
     * Delete all sensors.
     */
    public void deleteAll() {
        log.debug("Request to delete all Sensors");
        sensorRepository.deleteAll();
    }
}
