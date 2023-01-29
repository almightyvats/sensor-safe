import { Component, ViewChild } from '@angular/core';
import { BreakpointObserver } from '@angular/cdk/layout';
import { MatSidenav } from '@angular/material/sidenav';
import { delay, filter } from 'rxjs/operators';
import { NavigationEnd, Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import {AppService} from "./app.service";
import {SharedService} from "./shared.service";

@UntilDestroy()
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  @ViewChild(MatSidenav)
  private sidenav!: MatSidenav;
  stations: any = [];
  private sensor: any = [];

  constructor(private observer: BreakpointObserver, private router: Router, private appService: AppService,
              private sharedService: SharedService) {
    this.setup();
  }

  ngAfterViewInit() {
    this.observer
      .observe(['(max-width: 800px)'])
      .pipe(delay(1), untilDestroyed(this))
      .subscribe((res: any) => {
        if (res.matches) {
          this.sidenav.mode = 'over';
          this.sidenav.close();
        } else {
          this.sidenav.mode = 'side';
          this.sidenav.open();
        }
      });

    this.router.events
      .pipe(
        untilDestroyed(this),
        filter((e) => e instanceof NavigationEnd)
      )
      .subscribe(() => {
        if (this.sidenav.mode === 'over') {
          this.sidenav.close();
        }
      });
  }

  private setup() {
    this.getAllStations();
  }

  getAllStations() {
    this.appService.getAllStations().subscribe(data => {
      this.stations = data;
      this.setUpFirstStation(this.stations[0].id);
    });
  }

  setUpFirstStation(stationId: any) {
    this.sensor = this.stations.find((station: any) => station.id === stationId).sensors;
    this.sharedService.setSensor(this.sensor);
  }

  onClick(stationId: any) {
    // get sensor ids from station id
    this.sensor = this.stations.find((station: any) => station.id === stationId).sensors;
    this.sharedService.setSensor(this.sensor);
  }

  getCurrentSensors() {
    return this.sensor;
  }
}
