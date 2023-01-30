import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class SharedService {
  private stations = new BehaviorSubject<any>(null);
  stationsData = this.stations.asObservable();

  private currentStation = new BehaviorSubject<any>(null);
  currentStationData = this.currentStation.asObservable();

  setCurrentStation(data: any) {
    this.currentStation.next(data);
  }

  setStations(data: any) {
    this.stations.next(data);
  }
}
