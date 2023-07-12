package com.almightyvats.sensorsafe.factory;

import com.almightyvats.sensorsafe.factory.sensors.*;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SensorProperty;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import org.springframework.stereotype.Component;

@Component
public class SensorFactory {
    public static Sensor createSensor(String name, String uniqueHardwareName, SensorType sensorType,
                                      SensorProperty sensorProperty) {
        switch (sensorType) {
            case AIR_TEMPERATURE -> {
                return new AirTemperature(name, uniqueHardwareName, sensorType, sensorProperty);
            }
            case SOIL_TEMPERATURE -> {
                return new SoilTemperature(name, uniqueHardwareName, sensorType, sensorProperty);
            }
            case SOIL_WATER_CONTENT -> {
                return new SoilWaterContent(name, uniqueHardwareName, sensorType, sensorProperty);
            }
            case RELATIVE_HUMIDITY -> {
                return new RelativeHumidity(name, uniqueHardwareName, sensorType, sensorProperty);
            }
            case DENDROMETER -> {
                return new Dendrometer(name, uniqueHardwareName, sensorType, sensorProperty);
            }
            case PRECIPITATION -> {
                return new Precipitation(name, uniqueHardwareName, sensorType, sensorProperty);
            }
            case SOLAR_RADIATION -> {
                return new SolarRadiation(name, uniqueHardwareName, sensorType, sensorProperty);
            }
        }
        return null;
    }
}
