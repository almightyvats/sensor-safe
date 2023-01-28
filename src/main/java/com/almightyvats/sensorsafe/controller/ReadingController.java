package com.almightyvats.sensorsafe.controller;

import com.almightyvats.sensorsafe.core.util.ReadingPayload;
import com.almightyvats.sensorsafe.service.ReadingService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/reading")
public class ReadingController {

    @Autowired
    private ReadingService readingService;

    @PostMapping("/add")
    public ResponseEntity<?> saveReading(@RequestBody ReadingPayload reading) {
        readingService.save(reading);
        return ResponseEntity.ok("Reading saved successfully");
    }

    @GetMapping("/all/{name}")
    public ResponseEntity<List<Document>> getAllReadings(@PathVariable String name) {
        return ResponseEntity.ok(readingService.getReadingsBySensorName(name));
    }
}
