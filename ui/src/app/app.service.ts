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

  getStationById(id: string): Observable<any> {
    const url = this.stationBaseUrl + id;
    return this.http.get<any[]>(url);
  }

  saveStation(data: any): Observable<any> {
    const url = this.stationBaseUrl + 'add';
    return this.http.post<any[]>(url, data);
  }

  updateStation(id: string, data: any): Observable<any> {
    const url = this.stationBaseUrl + 'update/' + id;
    return this.http.put<any[]>(url, data);
  }

  deleteStation(id: string): Observable<any> {
    const url = this.stationBaseUrl + 'delete/' + id;
    return this.http.delete<any[]>(url);
  }
}
