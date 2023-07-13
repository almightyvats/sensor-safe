package com.almightyvats.sensorsafe.model.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // for testing
public class SensorProperty {
    @JsonProperty("isEnable")
    private boolean isEnable;
    private double maxValue;
    private double minValue;
    private String unit;
    private int precision;
    private double sleepInterval;
    private int maxFrozenTimeInSeconds;
    private double maxRateOfChange;
    private double minVariationCoefficient;
}
