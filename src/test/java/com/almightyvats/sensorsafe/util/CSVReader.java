package com.almightyvats.sensorsafe.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
public class CSVReader {
    private static final String TAG = "CSVReader";

    public static List<List<String>> readCSV(String filePath) {
        List<List<String>> csvData = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                // split line by comma
                String[] tokens = line.split(",");
                // add to list
                csvData.add(List.of(tokens));
            }
            reader.close();
        } catch (IOException e) {
            log.error(TAG, e);
        }
        return csvData;
    }

    public static Map<Date, Double> getValueWithTimestamp(String filePath, CSVMiemingSensorType sensorType,
                                                   int number_of_readings, int start_from) throws ParseException {
        // read csv file
        List<List<String>> miemingCSV = CSVReader.readCSV(filePath);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        TimeZone utcTime = TimeZone.getTimeZone("CET");
        formatter.setTimeZone(utcTime);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // loop through csv file
        Map<Date, Double> valueWithTimestamp = new HashMap<>();

        int noOfReadings = 0;
        for (int i = start_from; i < miemingCSV.size(); i++) {
            if (i == 0) {
                continue;
            }
            noOfReadings++;
            // get timestamp
            String timestamp = miemingCSV.get(i).get(CSVMiemingSensorType.TIMESTAMP.getIndex());
            Date timestamp_formatted = formatter.parse(timestamp);
            var instant = timestamp_formatted.toInstant();
            // convert instant to Date again but make sure its in utc
            Date timestamp_formatted_utc = Date.from(instant.atZone(ZoneOffset.UTC).toInstant());

            // get value
            Double value = Double.parseDouble(miemingCSV.get(i).get(sensorType.getIndex()));
            // add to map
            valueWithTimestamp.put(timestamp_formatted_utc, value);
            if (noOfReadings == number_of_readings) {
                break;
            }
        }
        return valueWithTimestamp;
    }
}