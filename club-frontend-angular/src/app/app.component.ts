import {Component, OnInit} from '@angular/core';
import {MainLayoutComponent} from "./core/layout/main-layout/main-layout.component";
import {AuthService} from "./core/auth/services/auth.service";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  imports: [MainLayoutComponent, MatProgressSpinner],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  public isLoaded = true;

  constructor(private authService: AuthService,
              private translate: TranslateService) {

    this.translate.addLangs(['en', 'fr-ca', 'es']);
    this.translate.setDefaultLang('en');
    this.translate.use(this.translate.getBrowserLang() || "en");
  }

  ngOnInit(): void {
    this.authService.isLoaded$.subscribe(isLoaded => {
      this.isLoaded = isLoaded;
    });
  }
}
