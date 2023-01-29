import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";

import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatListModule} from '@angular/material/list';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatSidenavModule} from "@angular/material/sidenav";
import {CardComponent} from './card/card.component';
import { MatDialogModule } from '@angular/material/dialog';
import { StationFormComponent } from './station.form/station.form.component';



@NgModule({
  declarations: [
    AppComponent,
    CardComponent,
    StationFormComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatButtonModule,
    MatListModule,
    MatToolbarModule,
    MatIconModule,
    MatSidenavModule,
    MatDialogModule
  ],
  exports: [CardComponent],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
