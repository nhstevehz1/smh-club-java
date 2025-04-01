import {Component} from '@angular/core';

import {TitlePageComponent} from '@app/shared/components/title-page/title-page.component';

@Component({
  selector: 'app-access-denied',
    imports: [
        TitlePageComponent
    ],
  templateUrl: './access-denied.component.html',
  styleUrl: './access-denied.component.scss'
})
export class AccessDeniedComponent {

}
