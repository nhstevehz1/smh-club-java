import {Component, OnInit} from '@angular/core';
import {MainLayoutComponent} from "./core/layout/main-layout/main-layout.component";
import {AuthService} from "./core/auth/services/auth.service";
import {MatProgressSpinner} from "@angular/material/progress-spinner";

@Component({
  selector: 'app-root',
  imports: [MainLayoutComponent, MatProgressSpinner],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  public isLoaded = true;

  constructor(public authService: AuthService) { }

  ngOnInit(): void {
    //this.authService.isLoaded$.subscribe(isLoaded => {
     // this.isLoaded = isLoaded;
    //});
  }
}
