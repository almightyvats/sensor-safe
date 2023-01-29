import {Component, ViewEncapsulation} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-station.form',
  templateUrl: './station.form.component.html',
  styleUrls: ['./station.form.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class StationFormComponent {

  constructor(public dialogRef: MatDialogRef<StationFormComponent>) {
  }
  onNoClick(): void {
    this.dialogRef.close();
  }

}
