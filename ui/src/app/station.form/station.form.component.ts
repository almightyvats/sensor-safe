import {Component, ViewEncapsulation, OnInit, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AppService} from "../app.service";
import {SharedService} from "../shared.service";

export interface IStation {
  name: string;
  macAddress: string;
  location: string;
}

@Component({
  selector: 'app-station.form',
  templateUrl: './station.form.component.html',
  styleUrls: ['./station.form.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class StationFormComponent implements OnInit {

  private TAG = "STATION_FORM";

  form!: FormGroup;
  station!: IStation;

  constructor(public dialogRef: MatDialogRef<StationFormComponent>, private apiService: AppService,
              private sharedService: SharedService, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) data: any) {
    this.station = data;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  submit() {
    if (this.form?.valid) {
      const formData = {...this.form.value};
      this.apiService.saveStation(formData).subscribe(() => {
        this.apiService.getAllStations().subscribe(data => {
          this.sharedService.setStations(data);
        });
      });
    }
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: new FormControl(this.station.name, [Validators.required]),
      mac: new FormControl(this.station.macAddress, [Validators.required]),
      location: new FormControl(this.station.location),
    });
  }
}
