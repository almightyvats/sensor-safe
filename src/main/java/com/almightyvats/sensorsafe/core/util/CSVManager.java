package com.almightyvats.sensorsafe.core.util;

import org.bson.Document;

import java.util.List;

public class CSVManager {
    //create a CSV file with a list of Documents get timestamp, sensorName, value, sanityFlag
    public static byte[] createCSVFile(List<Document> documents) {
        // create a CSV string
        StringBuilder csvBuilder = new StringBuilder();
        // TODO: add ,SANITY_FLAG to the header
        csvBuilder.append("TIMESTAMP,").append(documents.get(0).get("sensorName").toString()).append("\n");
        for (Document document : documents) {
            csvBuilder.append(document.get("timestamp").toString())
                    .append(",")
                    .append(document.get("value").toString())
                    .append("\n");
        }
        return csvBuilder.toString().getBytes();
    }
}
