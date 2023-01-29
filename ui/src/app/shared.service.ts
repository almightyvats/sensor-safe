import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class SharedService {
  private currentSensorIds = new BehaviorSubject<any>(null);
  currentData = this.currentSensorIds.asObservable();

  setSensor(data: any) {
    this.currentSensorIds.next(data);
  }
}
