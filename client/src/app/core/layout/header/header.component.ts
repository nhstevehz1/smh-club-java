import {Component, computed, inject, input, output} from '@angular/core';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";
import {DOCUMENT} from "@angular/common";
import {MatDividerModule} from "@angular/material/divider";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatSlideToggleChange, MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatIconButton} from "@angular/material/button";
import {LanguageSwitcherComponent} from "../../i18n/language-switcher/language-switcher.component";
import {TranslatePipe} from "@ngx-translate/core";

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
        LanguageSwitcherComponent,
        TranslatePipe
    ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  private document: Document = inject(DOCUMENT);

  userNameSignal
      = input<string | undefined>(undefined, {alias: 'userName'});

  isLoggedInSignal
      = input<boolean>(false, {alias: 'isLoggedIn'});

  showUserName = computed<boolean>(() =>
        !!this.userNameSignal() && this.isLoggedInSignal());

  profileClickSignal = output({alias: 'profileClick'});

  logoutClickSignal = output({alias: 'logoutClick'});

  toggleSidenavSignal = output({alias: 'toggleSidenav'});

  profileHandler() {
    this.profileClickSignal.emit();
  }

  logoutHandler() {
    this.logoutClickSignal.emit();
  }

  toggleSideNavHandler() {
      this.toggleSidenavSignal.emit();
  }

  onThemeChanged(event: MatSlideToggleChange): void {
      this.document.body.classList.toggle('dark-mode', event.checked);
  }
}
