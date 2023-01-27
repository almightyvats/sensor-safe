package com.almightyvats.sensorsafe.controller;

import com.almightyvats.sensorsafe.model.Reading;
import com.almightyvats.sensorsafe.service.ReadingService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/reading")
public class ReadingController {

    @Autowired
    private ReadingService readingService;

    @PostMapping("/add")
    public void saveReading(Reading reading) {
        readingService.save(reading);
    }

    @GetMapping("/all/{name}")
    public List<Document> getAllReadings(@PathVariable String name) {
        return readingService.getReadingsBySensorName(name);
    }
}
