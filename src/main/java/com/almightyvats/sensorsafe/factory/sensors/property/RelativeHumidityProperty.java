package com.almightyvats.sensorsafe.factory.sensors.property;

import com.almightyvats.sensorsafe.model.custom.SensorProperty;

public class RelativeHumidityProperty extends SensorProperty {

    public RelativeHumidityProperty(boolean isEnable, double maxValue, double minValue, String unit,
                                    int precision, double sleepInterval, int maxFrozenTimeInSeconds,
                                    double maxRateOfChange, double minVariationCoefficient) {
        super(isEnable, maxValue, minValue, unit, precision, sleepInterval, maxFrozenTimeInSeconds,
                maxRateOfChange, minVariationCoefficient);
    }
    // add more properties here
}
