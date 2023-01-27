package com.almightyvats.sensorsafe.model.custom;

import lombok.Data;

@Data
public class SensorProperty {
    private boolean isEnable;
    private double maxValue;
    private double minValue;
    private String unit;
    private int precision;
    private double sleepInterval;
    private int maxFrozenTimeInSeconds;
    private double maxRateOfChange;
    private double minVariationCoefficient;
    // Only for solar radiation
    private double latitude;
    private double longitude;
}
