import { Injectable } from '@angular/core';
import {BaseEditDialogService} from '../../../shared/components/edit-dialog/services/base-edit-dialog.service';
import {Email} from '../models/email';
import {MatDialog} from '@angular/material/dialog';

@Injectable({
  providedIn: 'root'
})
export class EmailEditDialogService extends BaseEditDialogService<Email> {
  constructor(dialog: MatDialog) {
    super(dialog);
  }
}
