package com.almightyvats.sensorsafe.repository;

import com.almightyvats.sensorsafe.model.Station;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends AbstractRepository<Station, String> {
}
