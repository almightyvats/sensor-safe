export interface ISensor {
  id?: string;
  name: string;
  uniqueHardwareName: string;
  type: string;
  parameters: {
    isEnable: boolean;
    maxValue: number;
    minValue: number;
    unit: string;
    precision: number;
    sleepInterval: number;
    maxFrozenTimeInSeconds: number;
    maxRateOfChange: number;
    minVariationCoefficient: number;
    // Only for solar radiation
    latitude: number;
    longitude: number;
  }
}
