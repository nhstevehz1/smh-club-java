import {Component, EventEmitter, inject, Input, Output} from '@angular/core';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";
import {DOCUMENT} from "@angular/common";
import {MatDividerModule} from "@angular/material/divider";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatSlideToggleChange, MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatIconButton} from "@angular/material/button";

@Component({
  selector: 'app-header',
    imports: [
        MatToolbarModule,
        MatMenuModule,
        MatIconModule,
        MatDividerModule,
        MatTooltipModule,
        MatSlideToggleModule,
        MatIconButton,
    ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  private document: Document = inject(DOCUMENT);

  @Input('name') name: string = '';

  @Input('isLoggedIn')
  isLoggedIn: boolean = false;

  @Input('lastLogin')
  lastLogin: string | null = null;

  @Output()
  profileClick = new EventEmitter<void>();

  @Output()
  logoutClick = new EventEmitter<void>();

  @Output()
  toggleSidenav = new EventEmitter<void>();

  profileHandler() {
    this.profileClick.next();
  }

  logoutHandler() {
    this.logoutClick.next();
  }

  toggleSideNavHandler() {
      this.toggleSidenav.next();
  }

  onThemeChanged(event: MatSlideToggleChange): void {
      this.document.body.classList.toggle('dark-mode', event.checked);
  }
}
