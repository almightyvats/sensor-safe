package com.almightyvats.sensorsafe.controller;

import com.almightyvats.sensorsafe.model.Station;
import com.almightyvats.sensorsafe.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/station")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping("/all")
    public ResponseEntity<List<Station>> getAllStations() {
        return ResponseEntity.ok(stationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable String id) {
        return ResponseEntity.ok(stationService.findById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Station> saveStation(@RequestBody Station station) {
        return ResponseEntity.ok(stationService.save(station));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Station> updateStation(@PathVariable String id, @RequestBody Station station) {
        return ResponseEntity.ok(stationService.update(id, station));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStation(@PathVariable String id) {
        return ResponseEntity.ok(stationService.delete(id));
    }
}
