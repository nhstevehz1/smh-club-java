import {Component, EventEmitter, inject, Input, Output} from '@angular/core';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";
import {DOCUMENT} from "@angular/common";
import {MatDividerModule} from "@angular/material/divider";
import {RouterLink} from "@angular/router";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatSlideToggleChange, MatSlideToggleModule} from "@angular/material/slide-toggle";

@Component({
  selector: 'app-header',
    imports: [
        MatToolbarModule,
        MatMenuModule,
        MatIconModule,
        MatDividerModule,
        RouterLink,
        MatTooltipModule,
        MatSlideToggleModule,
    ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  private document: Document = inject(DOCUMENT);

  @Input('name') name: string = '';

  @Input('isAuthed')
  isAuthed: boolean = false;

  @Input('lastLogin')
  lastLogin: string | null = null;

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

  onThemeChanged(event: MatSlideToggleChange): void {
      this.document.body.classList.toggle('dark-mode', event.checked);
  }
}
