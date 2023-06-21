import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CardFormComponent} from "../card.form/card.form.component";
import {StatsService} from "./stats.service";
import {IStats} from "./stats.interface";
import {IDates} from "./dates.interface";

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent implements OnInit {
  TAG = 'StatsComponent';
  stats!: IStats[];
  dates!: IDates;

  constructor(public dialogRef: MatDialogRef<CardFormComponent>, private sanityApi: StatsService,
              @Inject(MAT_DIALOG_DATA) data: any) {
    this.getSanityStatsBySensorId(data.sensorId);
    this.getFirstAndLastDate(data.sensorId);
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

  getFirstAndLastDate(id: string) {
    this.sanityApi.getFirstAndLastDate(id).subscribe((data: any) => {
      console.log(data);
      this.dates = data;
    });
  }
}
