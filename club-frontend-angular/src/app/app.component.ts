import {Component, OnInit} from '@angular/core';
import {MainLayoutComponent} from "./core/layout/main-layout/main-layout.component";
import {LoadingSpinnerService} from "./core/loading/loading-spinner.service";
import {AuthService} from "./core/auth/services/auth.service";
import {MatProgressSpinner} from "@angular/material/progress-spinner";

@Component({
  selector: 'app-root',
  imports: [MainLayoutComponent, MatProgressSpinner],
  providers: [LoadingSpinnerService],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  public isLoaded = false;

  constructor(public authService: AuthService) { }

  ngOnInit(): void {
    this.authService.isLoaded$.subscribe(isLoaded => {
      this.isLoaded = isLoaded;
    })
  }


}
