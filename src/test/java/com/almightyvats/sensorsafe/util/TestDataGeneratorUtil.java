package com.almightyvats.sensorsafe.util;

import com.almightyvats.sensorsafe.sanity.util.SanityCheckUtil;
import smile.math.MathEx;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
public class TestDataGeneratorUtil {
    private static final Random random = new Random();

    public static List<Double> generateValueOutOfBounds(Double minValue, Double maxValue,
                                                        int numberOfValues, int outOfBoundCount) {
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < numberOfValues - outOfBoundCount; i++) {
            values.add(minValue + (maxValue - minValue) * random.nextDouble());
        }
        for (int i = 0; i < outOfBoundCount; i++) {
            values.add(random.nextBoolean() ? minValue - (maxValue - minValue) * random.nextDouble() :
                    maxValue + (maxValue - minValue) * random.nextDouble());
        }
        return values;
    }

    public static List<Double> generateValueOutOfBoundsOfMaxRateOfChange(Double minValue, Double maxValue, int numberOfValues,
                                                                         int outOfBoundCount, Double maxRateOfChangePerHour, double timeDifferenceInHours) {
        numberOfValues = Math.min(numberOfValues, calculateNumberOfValuesWithRateOfChange(minValue,
                maxValue, maxRateOfChangePerHour));
        return generateValuesWithSteadyRateOfChange(minValue, maxRateOfChangePerHour, numberOfValues, outOfBoundCount, timeDifferenceInHours);
    }

    private static List<Double> generateValuesWithSteadyRateOfChange(double minValue, double rateOfChangePerHour,
                                                                    int numberOfValues, int numberOfValuesOutOfBound,
                                                                    double timeDifferenceInHours) {
        List<Double> values = new ArrayList<>();
        double currentValue = minValue;
        Random rand = new Random();
        for (int i = 0; i < numberOfValues - numberOfValuesOutOfBound; i++) {
            values.add(currentValue);
            if (rand.nextBoolean()) {
                currentValue += rateOfChangePerHour * timeDifferenceInHours;
            } else {
                currentValue -= rateOfChangePerHour * timeDifferenceInHours;
            }
        }
        for (int i = 0; i < numberOfValuesOutOfBound; i++) {
            currentValue += rateOfChangePerHour * timeDifferenceInHours + 0.25;
            values.add(currentValue);
        }
        return values;
    }

    private static int calculateNumberOfValuesWithRateOfChange(double minValue, double maxValue, double rateOfChange) {
        return (int) Math.floor((maxValue - minValue) / rateOfChange);
    }

    public static double findHighestRateOfChange(double[] values, boolean printRates) {
        double maxRateOfChange = 0.0;
        for (int i = 1; i < values.length; i++) {
            double rateOfChange = Math.abs(values[i] - values[i - 1]);
            if (printRates) {
                System.out.println("Rate of change between " + values[i - 1] + " and " + values[i] + ": " + rateOfChange);
            }
            if (rateOfChange > maxRateOfChange) {
                maxRateOfChange = rateOfChange;
            }
        }
        return maxRateOfChange;
    }

    public static double[] generateRandomValuesWithLowVariation(int numValues, double variationCoeff, double minValue, double maxValue) {
        Random rand = new Random();
        double[] values = new double[numValues];
        values[0] = rand.nextDouble() * (maxValue - minValue) + minValue;

        for (int i = 1; i < numValues; i++) {
            double maxDelta = values[i - 1] * variationCoeff;
            double delta = rand.nextDouble() * maxDelta * 2 - maxDelta;
            double newValue = values[i - 1] + delta;
            // Adjust the new value to be within the min and max bounds
            newValue = Math.max(minValue, Math.min(maxValue, newValue));
            values[i] = newValue;
        }

        return values;
    }

    public static int countValuesInRangeWithVariationCoefficient(double min, double max, double variationCoefficient) {
        double range = max - min;
        double standardDeviation = range * Math.sqrt(variationCoefficient);
        double mean = (max + min) / 2;
        double interval = 2 * standardDeviation / (Math.sqrt(3));
        return (int) Math.floor(range / interval);
    }

    public static Long[] generateTimestamps(int numberOfValues, long timeDifferenceInHours) {
        Long[] timestamps = new Long[numberOfValues];
        long timeDifference = 60 * 60 * 1000 * timeDifferenceInHours;

        // Generate initial timestamp
        timestamps[0] = System.currentTimeMillis();

        // Generate subsequent timestamps separated by one hour
        for (int i = 1; i < numberOfValues; i++) {
            timestamps[i] = timestamps[i - 1] + timeDifference;
        }

        return timestamps;
    }

    public static Long[] generateTimestampsWithInitial(int numberOfValues, long timeDifferenceInHours) {
        Long[] timestamps = new Long[numberOfValues];
        long timeDifference = 60 * 60 * 1000 * timeDifferenceInHours;

        // Generate initial timestamp
        int year = 2023;
        int month = 6;
        int dayOfMonth = 21;
        int hour = 15;
        int minute = 48;
        int second = 41;

        // Create a LocalDateTime object for the specific date and time
        LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);

        // Get the long timestamp value in milliseconds
        long timestampMillis = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        timestamps[0] = timestampMillis;

        // Generate subsequent timestamps separated by one hour
        for (int i = 1; i < numberOfValues; i++) {
            timestamps[i] = timestamps[i - 1] + timeDifference;
        }

        return timestamps;
    }

    public static void checkMaxRateOfChangePerHour(List<Double> values, Long[] timestamps, Double maxRateOfChangePerHour) {
        for (int i = 1; i < values.size(); i++) {
            double currentValue = values.get(i);
            double previousValue = values.get(i - 1);
            double timeDifferenceInHours = SanityCheckUtil.getTimeDifferenceInHours(new Date(timestamps[i]),
                    new Date(timestamps[i - 1]));
            double rateOfChange = Math.abs((currentValue - previousValue) / timeDifferenceInHours);
            if (rateOfChange > maxRateOfChangePerHour) {
                System.out.println("Rate of change is too high: " + rateOfChange + " > " + maxRateOfChangePerHour + " "
                        + currentValue + " " + previousValue + " " + timeDifferenceInHours);
            } else {
                System.out.println("Rate of change is ok: " + rateOfChange + " < " + maxRateOfChangePerHour + " " +
                        currentValue + " " + previousValue + " " + timeDifferenceInHours);
            }
        }
    }

    public static boolean isBelowVariationCoefficient(List<Double> values, double variationCoefficient) {
        double mean = MathEx.mean(values.stream().mapToDouble(Double::doubleValue).toArray());
        double sd = MathEx.sd(values.stream().mapToDouble(Double::doubleValue).toArray());

        return sd / mean > variationCoefficient;
    }
    public static double[] incomingDataForSpikeDetection() {
        return new double[] { 5.5, 6.7, 5.9, 7.1, 5.2, 12.3, 6.9, 8.1, 7.2, 6.5, 15.0, 7.6 };
    }
}
