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
        console.log(this.TAG, "submit", "data", data);
      });
    } else {
      console.log(this.TAG, "submit", "invalid form");
    }
  }

  update() {
    if (this.form.valid && this.sensor.id !== undefined) {
      const sensor: ISensor = this.getSensor();
      console.log(this.TAG, "update", "sensor", sensor);
      console.log(this.TAG, "update", "sensor.id", this.sensor.id);
      this.sensorApi.updateSensor(this.currentStationId, this.sensor.id, sensor).subscribe((data: any) => {
        console.log(this.TAG, "update", "data", data);
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
