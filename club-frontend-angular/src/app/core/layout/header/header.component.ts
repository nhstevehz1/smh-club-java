import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatToolbar} from "@angular/material/toolbar";
import {MatLabel} from "@angular/material/form-field";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {DateTimeFromIsoPipe} from "../../../shared/pipes/luxon/date-time-from-iso.pipe";
import {DateTimeToFormatPipe} from "../../../shared/pipes/luxon/date-time-to-format.pipe";
import {NgIf} from "@angular/common";
import {DateTime} from "luxon";

@Component({
  selector: 'app-header',
  imports: [
    MatToolbar,
    MatLabel,
    MatMenu,
    MatIcon,
    MatMenuItem,
    MatIconButton,
    MatMenuTrigger,
    DateTimeFromIsoPipe,
    DateTimeToFormatPipe,
    NgIf
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  @Input('name')
  name: string = '';

  @Input('isAuthed')
  isAuthed: boolean = false;

  @Input('lastLogin')
  lastLogin: string = DateTime.now().toISODate();

  @Output()
  profileClick = new EventEmitter<void>();

  @Output()
  logoutClick = new EventEmitter<void>();

  @Output()
  toggleSidenav = new EventEmitter<void>();

  profile() {
    this.profileClick.next();
  }

  logout() {
    this.logoutClick.next();
  }
}
