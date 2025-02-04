import { Component } from '@angular/core';
import {MatCard, MatCardHeader, MatCardSubtitle, MatCardTitle, MatCardTitleGroup} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-access-denied',
    imports: [
        MatCard,
        MatCardHeader,
        MatCardSubtitle,
        MatCardTitle,
        MatCardTitleGroup,
        MatIcon
    ],
  templateUrl: './access-denied.component.html',
  styleUrl: './access-denied.component.scss'
})
export class AccessDeniedComponent {

}
