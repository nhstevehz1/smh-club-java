import {Component} from '@angular/core';

import {TitlePageComponent} from '@app/shared/components/title-page/title-page.component';

@Component({
  selector: 'app-home',
    imports: [
        TitlePageComponent
    ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {}
