import {Component} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MainLayoutComponent} from "./core/layout/main-layout/main-layout.component";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
    imports: [MatProgressSpinner, MainLayoutComponent, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'club-frontend-angular';

  public loading: boolean = false;
}
