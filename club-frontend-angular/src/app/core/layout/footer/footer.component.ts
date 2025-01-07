import {Component} from '@angular/core';
import {DateTime} from "luxon";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
export class FooterComponent {

  get year(): string {
    return DateTime.local().toFormat("yyyy");
  }

}
