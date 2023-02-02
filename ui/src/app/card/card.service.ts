import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CardService {
  private sensorBaseUrl = 'http://localhost:8080/v1/sensor/';

  constructor(private http: HttpClient) { }

  getAllSensors(): Observable<any> {
    const url = this.sensorBaseUrl + 'all';
    return this.http.get(url);
  }

  getSensorById(id: string): Observable<any> {
    const url = this.sensorBaseUrl + id;
    return this.http.get(url);
  }

  saveSensor(station_id: string, sensor: Object): Observable<Object> {
    const url = this.sensorBaseUrl + 'add/' + station_id;
    return this.http.post(url, sensor);
  }

  updateSensor(station_id: string, id: string, value: any): Observable<Object> {
    const url = this.sensorBaseUrl + 'update/' + station_id + '/' + id;
    return this.http.put(url, value);
  }

  deleteSensor(id: string): Observable<any> {
    const url = this.sensorBaseUrl + 'delete/' + id;
    return this.http.delete(url, { responseType: 'text' });
  }
}
