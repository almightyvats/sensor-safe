import {Component} from '@angular/core';
import {CardService} from "./card.service";
import {SharedService} from "../shared.service";
import {MatDialog} from "@angular/material/dialog";
import {CardFormComponent} from "./card.form/card.form.component";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent {
  TAG = "CardComponent";
  sensors: any;
  currentSensor: any;
  currentStation!: any;

  constructor(private userService: CardService, private sharedService: SharedService, private dialog: MatDialog) {
    this.getSensors();
  }

  getSensors() {
    this.userService.getAllSensors().subscribe(data => {
      this.sensors = data;
      this.sharedService.currentStationData.subscribe(data => {
        this.currentStation = data;
        this.setCurrentSensor(this.currentStation.sensors);
      });
    });
  }

  openModal(sensorId: any) {
    this.dialog.open(CardFormComponent, {
      data: {},
      width: '100%',
      maxWidth: '600px',
    });
  }

  setCurrentSensor(sensorIds: any[]) {
    // populate this.currentSensor with the sensors that match the sensorIds
    this.currentSensor = this.sensors.filter((sensor: any) => sensorIds.includes(sensor.id));
  }
}
