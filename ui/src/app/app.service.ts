import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppService {
  private stationBaseUrl = 'http://localhost:8080/v1/station/';

  constructor(private http: HttpClient) { }

  getAllStations(): Observable<any> {
    const url = this.stationBaseUrl + 'all';
    return this.http.get<any[]>(url);
  }
}
