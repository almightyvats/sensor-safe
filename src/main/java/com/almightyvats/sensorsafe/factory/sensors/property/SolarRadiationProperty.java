package com.almightyvats.sensorsafe.factory.sensors.property;

import com.almightyvats.sensorsafe.model.custom.SensorProperty;

import lombok.Getter;
import lombok.Setter;

public class SolarRadiationProperty extends SensorProperty {
    public SolarRadiationProperty(boolean isEnable, double maxValue, double minValue, String unit, int precision,
                           double sleepInterval, int maxFrozenTimeInSeconds, double maxRateOfChange,
                           double minVariationCoefficient, double latitude, double longitude) {
        super(isEnable, maxValue, minValue, unit, precision, sleepInterval, maxFrozenTimeInSeconds,
                maxRateOfChange, minVariationCoefficient);
        this.latitude = latitude;
        this.longitude = longitude;
    }
    @Getter @Setter
    private double latitude;
    @Getter @Setter
    private double longitude;
}
