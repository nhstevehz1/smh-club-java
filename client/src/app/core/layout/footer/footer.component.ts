import {Component} from '@angular/core';
import {DateTime} from 'luxon';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
    selector: 'app-footer',
    templateUrl: './footer.component.html',
    imports: [
        TranslatePipe
    ],
    styleUrl: './footer.component.scss'
})
export class FooterComponent {

  getYear(): string {
    return DateTime.local().toFormat('yyyy');
  }

}
