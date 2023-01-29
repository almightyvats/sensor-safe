import {Component} from '@angular/core';
import {CardService} from "./card.service";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent {
  sensors: any;

  constructor(private userService: CardService) {
    this.setup();
  }

  private setup() {
    this.getSensors();
  }

  getSensors() {
    this.userService.getAllSensors().subscribe(data => {
      this.sensors = data;
    });
  }
}
