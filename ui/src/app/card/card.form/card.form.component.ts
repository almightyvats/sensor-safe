import {Component, ViewEncapsulation, OnInit, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AppService} from "../../app.service";
import {SharedService} from "../../shared.service";

export interface ISensor {
  id: string;
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

@Component({
  selector: 'app-card.form',
  templateUrl: './card.form.component.html',
  styleUrls: ['./card.form.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CardFormComponent implements OnInit {
  sensor!: ISensor;
  form!: FormGroup;

  constructor(public dialogRef: MatDialogRef<CardFormComponent>, private apiService: AppService,
              private sharedService: SharedService, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) data: any) {
    this.sensor = data;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit(): void {

    this.form = this.fb.group({
      name: new FormControl(this.sensor.name, [Validators.required]),
      type: new FormControl(this.sensor.type, [Validators.required]),
      // isEnable: new FormControl(this.sensor.parameters.isEnable, [Validators.requiredTrue]),
      // maxValue: new FormControl(this.sensor.parameters.maxValue, [Validators.required]),
      // minValue: new FormControl(this.sensor.parameters.minValue, [Validators.required]),
      // unit: new FormControl(this.sensor.parameters.unit, [Validators.required]),
      // precision: new FormControl(this.sensor.parameters.precision, [Validators.required]),
      // sleepInterval: new FormControl(this.sensor.parameters.sleepInterval, [Validators.required]),
      // maxFrozenTimeInSeconds: new FormControl(this.sensor.parameters.maxFrozenTimeInSeconds, [Validators.required]),
      // maxRateOfChange: new FormControl(this.sensor.parameters.maxRateOfChange, [Validators.required]),
      // minVariationCoefficient: new FormControl(this.sensor.parameters.minVariationCoefficient, [Validators.required]),
      // latitude: new FormControl(this.sensor.parameters.latitude, [Validators.required]),
      // longitude: new FormControl(this.sensor.parameters.longitude, [Validators.required]),
    });
  }


  submit() {

  }
}
