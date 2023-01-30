import {Component, ViewEncapsulation} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AppService} from "../app.service";

@Component({
  selector: 'app-station.form',
  templateUrl: './station.form.component.html',
  styleUrls: ['./station.form.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class StationFormComponent {

  private LABEL = "STATION_FORM";

  form = new FormGroup({
    name: new FormControl('', [Validators.required]),
    mac: new FormControl('', [Validators.required]),
    location: new FormControl(''),
  });

  constructor(public dialogRef: MatDialogRef<StationFormComponent>, private apiService: AppService) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  submit() {
    if (this.form.valid) {
      const formData = {...this.form.value};
      this.apiService.saveStation(formData).subscribe(res => {
        console.log(this.LABEL, "submit", "res", res);
      });
    }
  }
}
