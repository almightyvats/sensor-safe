import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import {AppService} from "./app.service";
import {CardService} from "./card/card.service";
@Injectable({
  providedIn: 'root'
})
export class SharedService {
  TAG = 'SharedService';
  constructor(private apiService: AppService, private cardService: CardService) { }

  private stations = new BehaviorSubject<any>(null);
  stationsData = this.stations.asObservable();
  private currentStation = new BehaviorSubject<any>(null);
  currentStationData = this.currentStation.asObservable();

  private sensors = new BehaviorSubject<any>(null);
  sensorsData = this.sensors.asObservable();

  setCurrentStation(data: any) {
    this.currentStation.next(data);
  }

  setStations(data: any, updateCurrentStation: boolean = false) {
    this.stations.next(data);
    if (updateCurrentStation) {
      const station = data.find((station: any) => station.id === this.currentStation.value.id);
      this.setCurrentStation(station);
    }
  }

   syncStations(updateCurrentStation: boolean = false) {
    this.apiService.getAllStations().subscribe(data => {
      this.setStations(data, updateCurrentStation);
    });
  }

  setSensors(data: any) {
    this.sensors.next(data);
  }

  syncSensors() {
    this.cardService.getAllSensors().subscribe(data => {
      this.setSensors(data);
    });
  }
}
