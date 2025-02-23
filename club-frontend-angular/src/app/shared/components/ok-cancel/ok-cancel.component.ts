import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatCardTitle} from "@angular/material/card";
import {MatFabButton} from "@angular/material/button";
import {MatTooltip} from "@angular/material/tooltip";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-ok-cancel',
  imports: [
    MatCardTitle,
    MatIcon,
    MatTooltip,
    MatFabButton
  ],
  templateUrl: './ok-cancel.component.html',
  styleUrl: './ok-cancel.component.scss'
})
export class OkCancelComponent {

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
