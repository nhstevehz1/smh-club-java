import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatCardTitle} from "@angular/material/card";
import {MatFabButton} from "@angular/material/button";
import {MatTooltip} from "@angular/material/tooltip";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-success',
  imports: [
    MatCardTitle,
    MatIcon,
    MatTooltip,
    MatFabButton
  ],
  templateUrl: './success.component.html',
  styleUrl: './success.component.scss'
})
export class SuccessComponent {

  @Input()
  public titleMessage = 'Success';

  @Input()
  public iconName = 'checked';

  @Input()
  public buttonToolTip = 'Ok';

  @Output()
  public buttonClick: EventEmitter<any> = new EventEmitter();

  onButtonClicked(): void {
    this.buttonClick.next(null);
  }
}
