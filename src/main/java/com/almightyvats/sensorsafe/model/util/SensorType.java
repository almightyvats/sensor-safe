package com.almightyvats.sensorsafe.model.util;

public enum SensorType {
    PRECIPITATION("PRECIPITATION"),
    SOLAR_RADIATION("SOLAR_RADIATION"),
    SOIL_WATER_CONTENT("SOIL_WATER_CONTENT"),
    SOIL_TEMPERATURE("SOIL_TEMPERATURE"),
    RELATIVE_HUMIDITY("RELATIVE_HUMIDITY"),
    AIR_TEMPERATURE("AIR_TEMPERATURE"),
    DENDROMETER("DENDROMETER");

    SensorType(String sensorType){
    }
}
