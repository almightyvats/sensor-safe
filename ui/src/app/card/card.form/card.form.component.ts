import {Component, ViewEncapsulation, OnInit, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {SharedService} from "../../shared.service";
import {CardService} from "../card.service";
import {ISensor} from "../sensor.interface";

@Component({
  selector: 'app-card.form',
  templateUrl: './card.form.component.html',
  styleUrls: ['./card.form.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CardFormComponent implements OnInit {
  TAG = "CARD_FORM";
  options = ['SOIL_TEMPERATURE', 'RELATIVE_HUMIDITY', 'AIR_TEMPERATURE', 'PRECIPITATION', 'SOLAR_RADIATION',
    'SOIL_WATER_CONTENT', 'DENDROMETER'];
  currentStationId = '';
  sensor!: ISensor;
  form!: FormGroup;
  showUpdateButton: boolean = false;

  tooltip: string[] = [
    "Unique name of the sensor",
    "Only enabled sensors will be considered for alerting and downloading data",
    "Unit of the sensor value",
    "Maximum value of the sensor",
    "Minimum value of the sensor",
    "Duration between two consecutive readings in minutes",
    "After the specified number of hours the sensor will be considered as frozen. Users will be " +
    "notified about the frozen sensor.",
    "Rate of change of the sensor value per hour. If the rate of change is greater than the specified value, " +
    "the reading will be flagged by no alert will be sent",
    "Latitudinal position of the sensor",
    "Longitudinal position of the sensor",
  ];

  constructor(public dialogRef: MatDialogRef<CardFormComponent>, private sensorApi: CardService,
              private sharedService: SharedService, @Inject(MAT_DIALOG_DATA) data: any) {
    this.sensor = data.sensor;
    this.currentStationId = data.stationId;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit(): void {
    this.showUpdateButton = this.sensor.id !== '';

    this.form = new FormGroup({
      name: new FormControl(this.sensor.name, [Validators.required]),
      type: new FormControl(this.sensor.type, [Validators.required]),
      isEnable: new FormControl(this.sensor.parameters.isEnable, [Validators.required]),
      maxValue: new FormControl(this.sensor.parameters.maxValue, [Validators.required]),
      minValue: new FormControl(this.sensor.parameters.minValue, [Validators.required]),
      unit: new FormControl(this.sensor.parameters.unit, [Validators.required]),
      precision: new FormControl(this.sensor.parameters.precision, [Validators.required]),
      sleepInterval: new FormControl(this.sensor.parameters.sleepInterval, [Validators.required]),
      maxFrozenTimeInSeconds: new FormControl(this.sensor.parameters.maxFrozenTimeInSeconds, [Validators.required]),
      maxRateOfChange: new FormControl(this.sensor.parameters.maxRateOfChange, [Validators.required]),
      minVariationCoefficient: new FormControl(this.sensor.parameters.minVariationCoefficient, [Validators.required]),
      latitude: new FormControl(this.sensor.parameters.latitude),
      longitude: new FormControl(this.sensor.parameters.longitude)
    });
  }


  submit() {
    if (this.form.valid) {
      const sensor: ISensor = this.getSensor();
      this.sensorApi.saveSensor(this.currentStationId, sensor).subscribe((data: any) => {
        this.sharedService.syncStations(true);
        this.sharedService.syncSensors();
      });
    } else {
      console.log(this.TAG, "submit", "invalid form");
    }
  }

  update() {
    if (this.form.valid && this.sensor.id !== undefined) {
      const sensor: ISensor = this.getSensor();
      this.sensorApi.updateSensor(this.currentStationId, this.sensor.id, sensor).subscribe((data: any) => {
        this.sharedService.syncStations(true);
        this.sharedService.syncSensors();
      });
    } else {
      console.log(this.TAG, "update", "invalid form");
    }
  }

  private getSensor() {
    return {
      name: this.form.value.name,
      uniqueHardwareName: '',
      type: this.form.value.type,
      parameters: {
        isEnable: this.form.value.isEnable,
        maxValue: this.form.value.maxValue,
        minValue: this.form.value.minValue,
        unit: this.form.value.unit,
        precision: this.form.value.precision,
        sleepInterval: this.form.value.sleepInterval,
        maxFrozenTimeInSeconds: this.form.value.maxFrozenTimeInSeconds,
        maxRateOfChange: this.form.value.maxRateOfChange,
        minVariationCoefficient: this.form.value.minVariationCoefficient,
        latitude: this.form.value.latitude,
        longitude: this.form.value.longitude
      }
    };
  }
}
