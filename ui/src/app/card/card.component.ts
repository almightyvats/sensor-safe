import {Component} from '@angular/core';
import {CardService} from "./card.service";
import {SharedService} from "../shared.service";
import {MatDialog} from "@angular/material/dialog";
import {CardFormComponent} from "./card.form/card.form.component";
import {ISensor} from "./sensor.interface";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent {
  TAG = "CardComponent";
  sensors!: ISensor[];
  currentSensors!: ISensor[];
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
    let selectedSensor: ISensor
      = {
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

    const sensor = this.sensors.find((sensor: any) => sensor.id === sensorId);

    const data = {
      sensor: sensorId !== null ? sensor : selectedSensor,
      stationId: this.currentStation.id,
    };

    this.dialog.open(CardFormComponent, {
      data: data,
      width: '100%',
      maxWidth: '600px',
    });
  }

  setCurrentSensor(sensorIds: any[]) {
    // populate this.currentSensor with the sensors that match the sensorIds
    this.currentSensors = this.sensors.filter((sensor: any) => sensorIds.includes(sensor.id));
  }
}
