package com.almightyvats.sensorsafe.util;

import com.almightyvats.sensorsafe.core.util.HardwareNameUtil;
import com.almightyvats.sensorsafe.core.util.ReadingPayload;
import com.almightyvats.sensorsafe.factory.SensorFactory;
import com.almightyvats.sensorsafe.factory.sensors.SolarRadiation;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.Station;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;

import java.util.Date;

public class ModelUtil {

    public static Sensor createSensor(String sensorId, String sensorName, SensorType sensorType) {
//        Sensor sensor = new Sensor();
//        sensor.setId(sensorId);
//        sensor.setName(sensorName);
//        sensor.setType(sensorType);

        SensorProperty sensorProperty = new SensorProperty();
        sensorProperty.setEnable(true);
        sensorProperty.setPrecision(2);
        // TODO: these sensor properties are not meaningful right now
        sensorProperty.setSleepInterval(15000);
        sensorProperty.setMaxFrozenTimeInSeconds(18000);
        // 5 hours in seconds = 18000
        sensorProperty.setMaxRateOfChange(0.5);
        sensorProperty.setMinVariationCoefficient(0.5);

        // set sensor properties with a switch on sensor type
        switch (sensorType) {
            case PRECIPITATION -> {
                sensorProperty.setUnit("mm");
                sensorProperty.setMaxValue(100);
                sensorProperty.setMinValue(0);
            }
            case SOLAR_RADIATION -> {
                sensorProperty.setUnit("W/m2");
                sensorProperty.setMaxValue(1000);
                sensorProperty.setMinValue(0);
                SolarRadiation solarRadiationSensor = (SolarRadiation)
                        SensorFactory.createSensor(sensorName,
                                HardwareNameUtil.getUniqueHardwareName(sensorName, sensorId),
                        sensorType, sensorProperty);
                solarRadiationSensor.setLatitude(0);
                solarRadiationSensor.setLongitude(0);
                return solarRadiationSensor;
            }
            case SOIL_WATER_CONTENT -> {
                sensorProperty.setUnit("m3/m3");
                sensorProperty.setMaxValue(1);
                sensorProperty.setMinValue(0);
            }
            case SOIL_TEMPERATURE -> {
                sensorProperty.setUnit("C");
                sensorProperty.setMaxValue(50);
                sensorProperty.setMinValue(-45);
            }
            case RELATIVE_HUMIDITY -> {
                sensorProperty.setUnit("%");
                sensorProperty.setMaxValue(100);
                sensorProperty.setMinValue(0);
            }
            case AIR_TEMPERATURE -> {
                sensorProperty.setUnit("C");
                sensorProperty.setMaxValue(50);
                sensorProperty.setMinValue(-50);
            }
            case DENDROMETER -> {
                sensorProperty.setUnit("m");
                sensorProperty.setMaxValue(100);
                sensorProperty.setMinValue(0);
            }
        }
        return SensorFactory.createSensor(sensorName, HardwareNameUtil.getUniqueHardwareName(sensorName, sensorId),
                sensorType, sensorProperty);
    }

    public static Station createStation(String stationId, String stationName, String macAddress, String stationLocation,
                                        String email) {
        Station station = new Station();
        station.setId(stationId);
        station.setName(stationName);
        station.setMacAddress(macAddress);
        station.setLocation(stationLocation);
        station.setEmail(email);
        return station;
    }

    public static ReadingPayload createReading(String sensorName, String stationMacAddress, long timestamp, double value) {
        ReadingPayload reading = new ReadingPayload();
        reading.setSensorName(sensorName);
        reading.setStationMacAddress(stationMacAddress);
//        reading.setUniqueHardwareName(HardwareNameUtil.getUniqueHardwareName(sensorName, stationMacAddress));
        reading.setTimestamp(timestamp/1000);
        reading.setValue(value);
        return reading;
    }
}
