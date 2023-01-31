import {Component, ViewEncapsulation, OnInit, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AppService} from "../app.service";
import {SharedService} from "../shared.service";

export interface IStation {
  id: string;
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
  showUpdateButton: boolean = false;

  constructor(public dialogRef: MatDialogRef<StationFormComponent>, private apiService: AppService,
              private sharedService: SharedService, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) data: any) {
    this.station = data;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit(): void {
    this.showUpdateButton = this.station.id !== undefined;

    this.form = this.fb.group({
      name: new FormControl(this.station.name, [Validators.required]),
      macAddress: new FormControl(this.station.macAddress, [Validators.required]),
      location: new FormControl(this.station.location),
    });
  }

  submit() {
    if (this.form?.valid) {
      const formData = {...this.form.value};
      this.apiService.saveStation(formData).subscribe(() => {
        this.syncStations();
      });
    }
  }

  update() {
    if (this.form?.valid) {
      const formData = {...this.form.value};
      this.apiService.updateStation(this.station.id, formData).subscribe(() => {
        this.syncStations();
      });
    }
  }

  private syncStations() {
    this.apiService.getAllStations().subscribe(data => {
      this.sharedService.setStations(data);
    });
  }
}
