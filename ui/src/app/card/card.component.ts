import {Component, OnInit} from '@angular/core';
import {CardService} from "./card.service";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {
  users: any;

  constructor(private userService: CardService) {
  }

  ngOnInit() {
    this.getUsers();
  }

  getUsers() {
    this.userService.getUsers().subscribe(data => {
      this.users = data;
    });
  }
}
