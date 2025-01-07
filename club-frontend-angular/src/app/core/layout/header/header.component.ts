import {Component, EventEmitter, inject, Input, Output} from '@angular/core';
import {MatToolbar} from "@angular/material/toolbar";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {DOCUMENT} from "@angular/common";
import {MatDivider} from "@angular/material/divider";
import {RouterLink} from "@angular/router";
import {MatTooltip} from "@angular/material/tooltip";
import {MatSlideToggle, MatSlideToggleChange} from "@angular/material/slide-toggle";

@Component({
  selector: 'app-header',
    imports: [
        MatToolbar,
        MatMenu,
        MatIcon,
        MatMenuItem,
        MatIconButton,
        MatMenuTrigger,
        MatDivider,
        RouterLink,
        MatTooltip,
        MatSlideToggle
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
