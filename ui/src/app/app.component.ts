import {Component, OnInit, ViewChild} from '@angular/core';
import {BreakpointObserver} from '@angular/cdk/layout';
import {MatSidenav} from '@angular/material/sidenav';
import {delay, filter} from 'rxjs/operators';
import {NavigationEnd, Router} from '@angular/router';
import {UntilDestroy, untilDestroyed} from '@ngneat/until-destroy';
import {AppService} from "./app.service";
import {SharedService} from "./shared.service";
import {MatDialog} from "@angular/material/dialog";
import {StationFormComponent} from "./station.form/station.form.component";

@UntilDestroy()
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  TAG = "AppComponent";
  @ViewChild(MatSidenav)
  private sidenav!: MatSidenav;
  stations: any = [];
  private sensor: any = [];

  constructor(private observer: BreakpointObserver, private router: Router, private appService: AppService,
              private sharedService: SharedService, private dialog: MatDialog) {
    this.setup();
  }

  ngOnInit(): void {
    this.populateUI();
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
      this.sharedService.stationsData.subscribe(data => {
        if (data !== null && data.length > 0) {
          this.setStationInSharedService(data.at(0));
        }
      });
  }

  getAllStations() {
    this.appService.getAllStations().subscribe(data => {
      this.sharedService.setStations(data);
    });
  }

  populateUI() {
    this.sharedService.stationsData.subscribe(data => {
      this.stations = data;
    });
  }

  setStationInSharedService(station: any) {
    this.sharedService.setCurrentStation(station);
  }

  onClick(stationId: any) {
    // get station with stationId
    const station = this.stations.find((station: any) => station.id === stationId);
    this.setStationInSharedService(station);
  }

  openModal(stationId: any) {
    const data = stationId !== null ? this.stations.find((station: any) => station.id === stationId) : {};
    this.dialog.open(StationFormComponent, {
      data: data,
      width: '100%',
      maxWidth: '600px',
    });
  }
}
