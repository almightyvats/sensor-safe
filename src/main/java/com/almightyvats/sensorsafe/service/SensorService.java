package com.almightyvats.sensorsafe.service;

import com.almightyvats.sensorsafe.core.util.HardwareNameUtil;
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
    public void delete(String id) {
        log.debug("Request to delete Sensor : {}", id);
        sensorRepository.deleteById(id);
    }

    /**
     * Save a sensor.
     *
     * @param sensor the entity to save.
     * @return the persisted entity.
     */
    public Sensor save(Sensor sensor, String stationId) {
        log.debug("Request to save Sensor : {}", sensor);
        Station station = stationService.findById(stationId);
        if (station == null) {
            log.error("Station with id {} does not exist", stationId);
            return null;
        }
        sensor.setUniqueHardwareName(HardwareNameUtil.getUniqueHardwareName(sensor.getName(), station.getMacAddress()));
        if (sensorRepository.existsByName(sensor.getName())) {
            log.error("Sensor with name {} already exists", sensor.getName());
            return null;
        }
        Sensor savedSensor = sensorRepository.save(sensor);
        stationService.addSensor(station.getId(), savedSensor.getId());
        return savedSensor;
    }

    /**
     * Update a sensor.
     *
     * @param sensorToUpdate the entity to update.
     * @return the persisted entity.
     */
    public Sensor update(Sensor sensorToUpdate, String stationId) {
        log.debug("Request to update Sensor : {}", sensorToUpdate);
        Sensor sensor = sensorRepository.findById(sensorToUpdate.getId()).orElse(null);
        if (sensor == null) {
            log.error("Sensor with id {} does not exist", sensorToUpdate.getId());
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
        sensor.setParameters(sensorToUpdate.getParameters());
        return sensorRepository.save(sensor);
    }
}
