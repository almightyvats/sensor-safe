package com.almightyvats.sensorsafe.core.util;

import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import com.almightyvats.sensorsafe.service.SensorService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Component
public class CSVManager {
    private static SensorService sensorService;

    @Autowired
    public CSVManager(SensorService sensorService) {
        CSVManager.sensorService = sensorService;
    }

    //create a CSV file with a list of Documents get timestamp, sensorName, value, sanityFlag
    public static byte[] createCSVFile(List<Document> documents) {
        // create a CSV string
        StringBuilder csvBuilder = new StringBuilder();
        String sensorName = documents.get(0).get("sensorName").toString();
        Sensor sensor = sensorService.findByName(sensorName);
        SensorType sensorType = sensor.getType();
        String unit = sensor.getParameters().getUnit();

        csvBuilder.append("TIMESTAMP,")
                .append(sensorType.toString()).append("[").append(unit)
                .append(",")
                .append(documents.get(0).get("sensorName").toString())
                .append("],")
                .append("SANITY_FLAGS").append("\n");
        for (Document document : documents) {
            Date timestamp = document.getDate("timestamp");
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            csvBuilder.append(timestamp.toString())
                    .append(",")
                    .append(document.get("value").toString())
                    .append(",")
                    .append(document.getList("sanityFlag", String.class).toString())
                    .append("\n");
        }
        return csvBuilder.toString().getBytes();
    }
}
