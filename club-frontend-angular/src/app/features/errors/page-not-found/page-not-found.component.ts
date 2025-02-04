import { Component } from '@angular/core';
import {MatCard, MatCardHeader, MatCardSubtitle, MatCardTitle, MatCardTitleGroup} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-page-not-found',
    imports: [
        MatCard,
        MatCardHeader,
        MatCardSubtitle,
        MatCardTitle,
        MatCardTitleGroup,
        MatIcon
    ],
  templateUrl: './page-not-found.component.html',
  styleUrl: './page-not-found.component.scss'
})
export class PageNotFoundComponent {

}
