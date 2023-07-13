package com.almightyvats.sensorsafe.factory;

import com.almightyvats.sensorsafe.factory.sensors.*;
import com.almightyvats.sensorsafe.factory.sensors.property.*;
import com.almightyvats.sensorsafe.model.Sensor;
import com.almightyvats.sensorsafe.model.custom.SensorType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SensorFactory {
    public static Sensor createSensor(String name, String uniqueHardwareName, SensorType sensorType,
                                      Map<String, Object> properties) {
        switch (sensorType) {
            case AIR_TEMPERATURE -> {
                AirTemperatureProperty airTemperatureProperty = new AirTemperatureProperty(
                        (boolean) properties.get("isEnable"),
                        (double) properties.get("maxValue"),
                        (double) properties.get("minValue"),
                        (String) properties.get("unit"),
                        (int) properties.get("precision"),
                        (double) properties.get("sleepInterval"),
                        (int) properties.get("maxFrozenTimeInSeconds"),
                        (double) properties.get("maxRateOfChange"),
                        (double) properties.get("minVariationCoefficient")
                );
                return new AirTemperature(name, uniqueHardwareName, sensorType, airTemperatureProperty);
            }
            case SOIL_TEMPERATURE -> {
                SoilTemperatureProperty soilTemperatureProperty = new SoilTemperatureProperty(
                        (boolean) properties.get("isEnable"),
                        (double) properties.get("maxValue"),
                        (double) properties.get("minValue"),
                        (String) properties.get("unit"),
                        (int) properties.get("precision"),
                        (double) properties.get("sleepInterval"),
                        (int) properties.get("maxFrozenTimeInSeconds"),
                        (double) properties.get("maxRateOfChange"),
                        (double) properties.get("minVariationCoefficient")
                );
                return new SoilTemperature(name, uniqueHardwareName, sensorType, soilTemperatureProperty);
            }
            case SOIL_WATER_CONTENT -> {
                SoilWaterContentProperty soilWaterContentProperty = new SoilWaterContentProperty(
                        (boolean) properties.get("isEnable"),
                        (double) properties.get("maxValue"),
                        (double) properties.get("minValue"),
                        (String) properties.get("unit"),
                        (int) properties.get("precision"),
                        (double) properties.get("sleepInterval"),
                        (int) properties.get("maxFrozenTimeInSeconds"),
                        (double) properties.get("maxRateOfChange"),
                        (double) properties.get("minVariationCoefficient")
                );
                return new SoilWaterContent(name, uniqueHardwareName, sensorType, soilWaterContentProperty);
            }
            case RELATIVE_HUMIDITY -> {
                RelativeHumidityProperty relativeHumidityProperty = new RelativeHumidityProperty(
                        (boolean) properties.get("isEnable"),
                        (double) properties.get("maxValue"),
                        (double) properties.get("minValue"),
                        (String) properties.get("unit"),
                        (int) properties.get("precision"),
                        (double) properties.get("sleepInterval"),
                        (int) properties.get("maxFrozenTimeInSeconds"),
                        (double) properties.get("maxRateOfChange"),
                        (double) properties.get("minVariationCoefficient")
                );
                return new RelativeHumidity(name, uniqueHardwareName, sensorType, relativeHumidityProperty);
            }
            case DENDROMETER -> {
                DendrometerProperty dendrometerProperty = new DendrometerProperty(
                        (boolean) properties.get("isEnable"),
                        (double) properties.get("maxValue"),
                        (double) properties.get("minValue"),
                        (String) properties.get("unit"),
                        (int) properties.get("precision"),
                        (double) properties.get("sleepInterval"),
                        (int) properties.get("maxFrozenTimeInSeconds"),
                        (double) properties.get("maxRateOfChange"),
                        (double) properties.get("minVariationCoefficient")
                );
                return new Dendrometer(name, uniqueHardwareName, sensorType, dendrometerProperty);
            }
            case PRECIPITATION -> {
                PrecipitationProperty precipitationProperty = new PrecipitationProperty(
                        (boolean) properties.get("isEnable"),
                        (double) properties.get("maxValue"),
                        (double) properties.get("minValue"),
                        (String) properties.get("unit"),
                        (int) properties.get("precision"),
                        (double) properties.get("sleepInterval"),
                        (int) properties.get("maxFrozenTimeInSeconds"),
                        (double) properties.get("maxRateOfChange"),
                        (double) properties.get("minVariationCoefficient")
                );
                return new Precipitation(name, uniqueHardwareName, sensorType, precipitationProperty);
            }
            case SOLAR_RADIATION -> {
                SolarRadiationProperty solarRadiationProperty = new SolarRadiationProperty(
                        (boolean) properties.get("isEnable"),
                        (double) properties.get("maxValue"),
                        (double) properties.get("minValue"),
                        (String) properties.get("unit"),
                        (int) properties.get("precision"),
                        (double) properties.get("sleepInterval"),
                        (int) properties.get("maxFrozenTimeInSeconds"),
                        (double) properties.get("maxRateOfChange"),
                        (double) properties.get("minVariationCoefficient"),
                        (double) properties.get("longitude"),
                        (double) properties.get("latitude")
                );
                return new SolarRadiation(name, uniqueHardwareName, sensorType, solarRadiationProperty);
            }
        }
        return null;
    }
}
