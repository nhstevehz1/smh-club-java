import {Component, input} from '@angular/core';
import {TranslatePipe} from '@ngx-translate/core';
import {DateTime} from 'luxon';

@Component({
  selector: 'app-data-display',
  imports: [
    TranslatePipe
  ],
  templateUrl: './data-display.component.html',
  styleUrl: './data-display.component.scss'
})
export class DataDisplayComponent {
  label = input<string>();
  data = input.required<string | number | DateTime | null>();
}
