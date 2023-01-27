package com.almightyvats.sensorsafe.service;

import com.almightyvats.sensorsafe.model.Station;
import com.almightyvats.sensorsafe.repository.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    /**
     * Get all the stations.
     *
     * @return the list of entities.
     */
    public List<Station> findAll() {
        log.debug("Request to get all Stations");
        return stationRepository.findAll();
    }

    /**
     * Get one station by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Station findById(String id) {
        log.debug("Request to get Station : {}", id);
        return stationRepository.findById(id).orElse(null);
    }

    /**
     * Delete the station by id.
     *
     * @param id the id of the entity.
     */
    public String delete(String id) {
        log.debug("Request to delete Station : {}", id);
        stationRepository.deleteById(id);
        return id;
    }

    /**
     * Save a station.
     *
     * @param station the entity to save.
     * @return the persisted entity.
     */
    public Station save(Station station) {
        if(stationRepository.existsByName(station.getName())) {
            log.error("Station with name {} already exists", station.getName());
            return null;
        }
        log.debug("Request to save Station : {}", station);
        return stationRepository.save(station);
    }

    /**
     * Update a station.
     *
     * @param stationToUpdate the entity to update.
     * @return the persisted entity.
     */
    public Station update(String id, Station stationToUpdate) {
        log.debug("Request to update Station : {}", stationToUpdate);
        Station station = stationRepository.findById(id).orElse(null);
        if(station == null) {
            log.error("Station with id {} does not exist", stationToUpdate.getId());
            return null;
        }
        station.setName(stationToUpdate.getName());
        station.setSensors(stationToUpdate.getSensors());
        station.setMacAddress(stationToUpdate.getMacAddress());
        station.setLocation(stationToUpdate.getLocation());
        return stationRepository.save(station);
    }

    public void addSensor(String stationId, String sensorId) {
        log.debug("Request to add sensor {} to station {}", sensorId, stationId);
        Station station = stationRepository.findById(stationId).orElse(null);
        if(station == null) {
            log.error("Station with id {} does not exist", stationId);
            return;
        }
        station.getSensors().add(sensorId);
        stationRepository.save(station);
    }
}
