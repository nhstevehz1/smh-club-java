import { Component } from '@angular/core';

import {BaseEditDialogComponent} from '@app/shared/components/base-edit-dialog/base-edit-dialog.component';

@Component({
  selector: 'app-edit-email-dialog',
  imports: [
    BaseEditDialogComponent
  ],
  templateUrl: './edit-email-dialog.component.html',
  styleUrl: './edit-email-dialog.component.scss'
})
export class EditEmailDialogComponent {

}
