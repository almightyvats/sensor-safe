import {Component} from '@angular/core';
import {CardService} from "./card.service";
import {SharedService} from "../shared.service";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent {
  TAG = "CardComponent";
  sensors: any;
  currentSensor: any;

  constructor(private userService: CardService, private sharedService: SharedService) {
    this.getSensors();
  }

  getSensors() {
    this.userService.getAllSensors().subscribe(data => {
      this.sensors = data;
      this.sharedService.currentStationData.subscribe(data => {
        this.setCurrentSensor(data.sensors);
      });
    });
  }

  setCurrentSensor(sensorIds: any[]) {
    // populate this.currentSensor with the sensors that match the sensorIds
    this.currentSensor = this.sensors.filter((sensor: any) => sensorIds.includes(sensor.id));
  }
}
