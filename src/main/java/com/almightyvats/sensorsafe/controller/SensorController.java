package com.almightyvats.sensorsafe.controller;

import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/sensor")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @GetMapping("/all")
    public List<Sensor> getAllSensors() {
        return sensorService.findAll();
    }

    @GetMapping("/{id}")
    public Sensor getSensorById(@PathVariable String id) {
        return sensorService.findById(id);
    }

    @PostMapping("/add/{station_id}")
    public Sensor saveSensor(@PathVariable String station_id, @RequestBody Sensor sensor) {
        return sensorService.save(station_id, sensor);
    }

    @PutMapping("/update/{station_id}/{sensor_id}")
    public Sensor updateSensor(@PathVariable String station_id, @PathVariable String sensor_id, @RequestBody Sensor sensor) {
        return sensorService.update(station_id, sensor_id, sensor);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSensor(@PathVariable String id) {
        return sensorService.delete(id);
    }
}
