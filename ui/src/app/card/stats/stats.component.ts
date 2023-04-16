import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CardFormComponent} from "../card.form/card.form.component";
import {StatsService} from "./stats.service";
import {IStats} from "./stats.interface";

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent implements OnInit {
  TAG = 'StatsComponent';
  stats!: IStats[];

  constructor(public dialogRef: MatDialogRef<CardFormComponent>, private sanityApi: StatsService,
              @Inject(MAT_DIALOG_DATA) data: any) {
    this.getSanityStatsBySensorId(data.sensorId);
  }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  getSanityStatsBySensorId(id: string) {
    this.sanityApi.getSanityStatsBySensorId(id).subscribe((data: any) => {
      console.log(data);
      this.stats = data;
    });
  }
}
