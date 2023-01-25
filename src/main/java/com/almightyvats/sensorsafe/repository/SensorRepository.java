package com.almightyvats.sensorsafe.repository;

import com.almightyvats.sensorsafe.model.Sensor;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends AbstractRepository<Sensor, String> {
}
