package com.almightyvats.sensorsafe.service;

import com.almightyvats.sensorsafe.model.Sensor;
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
    public Sensor save(Sensor sensor) {
        // TODO: Create unique name using station mac and name
        // will probably have to get the sensor id with the request
        log.debug("Request to save Sensor : {}", sensor);
        if (sensorRepository.existsByName(sensor.getName())) {
            log.error("Sensor with name {} already exists", sensor.getName());
            return null;
        }
        return sensorRepository.save(sensor);
    }
}
