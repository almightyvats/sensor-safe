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
  TAG = "CARD_FORM";
  options = ['SOIL_TEMPERATURE', 'RELATIVE_HUMIDITY', 'AIR_TEMPERATURE', 'PRECIPITATION', 'SOLAR_RADIATION',
    'SOIL_WATER_CONTENT', 'DENDROMETER'];

  sensor: ISensor = {
    id: '',
    name: '',
    uniqueHardwareName: '',
    type: '',
    parameters: {
      isEnable: false,
      maxValue: 0,
      minValue: 0,
      unit: '',
      precision: 0,
      sleepInterval: 0,
      maxFrozenTimeInSeconds: 0,
      maxRateOfChange: 0,
      minVariationCoefficient: 0,
      latitude: 0,
      longitude: 0,
    }
  };
  form!: FormGroup;
  toggleValue = new FormControl(false);
  dropdownValue = new FormControl('AIR_TEMPERATURE');
  textboxValue = new FormControl('', [Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/)]);

  constructor(public dialogRef: MatDialogRef<CardFormComponent>, private apiService: AppService,
              private sharedService: SharedService, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) data: any) {
    // this.sensor = data; TODO: uncomment this line to pass data from parent component
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit(): void {
    console.log(this.TAG, "ngOnInit", this.sensor);

    this.form= new FormGroup({
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
      console.log(this.TAG, "submit", this.form.value);
    } else {
  console.log(this.TAG, "submit", "invalid form");
    }
  }
}
