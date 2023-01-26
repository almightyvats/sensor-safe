package com.almightyvats.sensorsafe.core.util;

public class HardwareNameUtil {
    public static String getUniqueHardwareName(String sensorName, String stationMacAddress) {
        return sensorName + "_" + stationMacAddress;
    }
}
