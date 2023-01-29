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
}
