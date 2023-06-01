package com.almightyvats.sensorsafe.core.util;

import org.bson.Document;

import java.util.List;

public class CSVManager {
    //create a CSV file with a list of Documents get timestamp, sensorName, value, sanityFlag
    public static byte[] createCSVFile(List<Document> documents) {
        // create a CSV string
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("TIMESTAMP,").append(documents.get(0).get("sensorName").toString())
                .append(",")
                .append("SANITY_FLAGS").append("\n");
        for (Document document : documents) {
            csvBuilder.append(document.get("timestamp").toString())
                    .append(",")
                    .append(document.get("value").toString())
                    .append(",")
                    .append(document.getList("sanityFlag", String.class).toString())
                    .append("\n");
        }
        return csvBuilder.toString().getBytes();
    }
}
