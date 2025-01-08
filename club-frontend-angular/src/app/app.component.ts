import {Component, inject, OnInit} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MainLayoutComponent} from "./core/layout/main-layout/main-layout.component";
import {NgIf} from "@angular/common";
import {LoadingSpinnerService} from "./core/loading/loading-spinner.service";
import {delay, take} from "rxjs";

@Component({
  selector: 'app-root',
  imports: [MainLayoutComponent],
  providers: [LoadingSpinnerService],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Social Club';
}
