import {Component, input, output} from '@angular/core';
import {MatCardTitle} from "@angular/material/card";
import {MatFabButton} from "@angular/material/button";
import {MatTooltip} from "@angular/material/tooltip";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-ok-cancel',
    imports: [
        MatCardTitle,
        MatIcon,
        MatTooltip,
        MatFabButton,
        TranslatePipe
    ],
  templateUrl: './ok-cancel.component.html',
  styleUrl: './ok-cancel.component.scss'
})
export class OkCancelComponent {

  title = input<string>('okCancel.defaultTitleMessage');

  iconName = input<string>('checked');

  buttonToolTip = input<string>('okCancel.defaultToolTip');

  buttonClick = output();

  onButtonClicked(): void {
    this.buttonClick.emit();
  }
}
