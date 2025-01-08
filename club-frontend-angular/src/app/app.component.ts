import {Component} from '@angular/core';
import {MainLayoutComponent} from "./core/layout/main-layout/main-layout.component";
import {LoadingSpinnerService} from "./core/loading/loading-spinner.service";

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
