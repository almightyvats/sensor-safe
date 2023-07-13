import {Component, OnInit} from '@angular/core';
import {CardService} from "./card.service";
import {SharedService} from "../shared.service";
import {MatDialog} from "@angular/material/dialog";
import {CardFormComponent} from "./card.form/card.form.component";
import {ISensor} from "./sensor.interface";
import {StatsComponent} from "./stats/stats.component";
import {StatsService} from "./stats/stats.service";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {
  TAG = "CardComponent";
  sensors!: ISensor[];
  currentSensors!: ISensor[];
  currentStation!: any;

  tooltip: string[] = ["Alerts are enabled for this sensor.",
  "Alerts are disabled for this sensor.",
  "Download flagged data for this sensor."];
  constructor(private userService: CardService, private sharedService: SharedService, private dialog: MatDialog,
              private statService: StatsService) {
    this.getAllSensors();
  }

  ngOnInit(): void {
    this.populateUI();
  }

  getAllSensors() {
    this.userService.getAllSensors().subscribe(data => {
      this.sharedService.setSensors(data);
    });
  }

  populateUI() {
    this.sharedService.sensorsData.subscribe(allSensors => {
      this.sensors = allSensors;
      this.sharedService.currentStationData.subscribe(currentStation => {
        this.currentStation = currentStation;
        this.setCurrentSensor(this.currentStation?.sensors);
      });
    });
  }

  setCurrentSensor(sensorIds: any[]) {
    // populate this.currentSensor with the sensors that match the sensorIds
    this.currentSensors = this.sensors?.filter((sensor: any) => sensorIds.includes(sensor.id));
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

  openStats(sensorId: any) {
    const data = {
      sensorId: sensorId,
    };

    this.dialog.open(StatsComponent, {
      data: data,
      width: '500%',
      maxWidth: '600px',
    });
  }

  download(sensorId: any) {
    this.statService.downloadSensorData(sensorId).subscribe((res: any) => {
      const blob = new Blob([res], { type: 'text/csv' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.setAttribute('style', 'display:none;');
      document.body.appendChild(a);
      a.href = url;
      a.download = 'example.csv';
      a.click();
      window.URL.revokeObjectURL(url);
      a.remove(); // remove the element from the DOM
    });
  }
}
