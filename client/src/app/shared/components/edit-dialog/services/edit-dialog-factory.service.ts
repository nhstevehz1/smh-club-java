import { Injectable } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {EditDialogData} from '../models/edit-event';
import {EditDialogService} from './edit-dialog.service';
import {EditDialogComponent} from '../edit-dialog.component';
import {first} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EditDialogFactoryService<T> {

  constructor(private dialog: MatDialog) { }

  open(dialogData: EditDialogData<T>): EditDialogService<T> {
    const dialogRef = this.dialog.open<EditDialogComponent<T>, EditDialogData<T>>(
      EditDialogComponent, {data: dialogData});

    dialogRef.afterClosed().pipe(first());

    return new EditDialogService(dialogRef);
  }
}
