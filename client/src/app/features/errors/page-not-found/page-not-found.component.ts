import {Component} from '@angular/core';

import {TitlePageComponent} from "@app/shared/components/title-page";

@Component({
  selector: 'app-page-not-found',
    imports: [
        TitlePageComponent,
    ],
  templateUrl: './page-not-found.component.html',
  styleUrl: './page-not-found.component.scss'
})
export class PageNotFoundComponent {

}
