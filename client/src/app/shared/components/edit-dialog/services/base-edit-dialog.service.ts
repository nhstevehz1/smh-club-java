import {MatDialog} from '@angular/material/dialog';
import {EditDialogInput, EditDialogResult} from '../models';
import {EditDialogComponent} from '../edit-dialog.component';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

export abstract class BaseEditDialogService<T> {

  protected constructor(private dialog: MatDialog) { }

  openDialog(dialogData: EditDialogInput<T>): Observable<EditDialogResult<T>> {
    const dialogRef = this.dialog.open<EditDialogComponent<T>, EditDialogInput<T>>(
      EditDialogComponent<T>, {data: dialogData});

    return dialogRef.afterClosed().pipe(
      map((result: EditDialogInput<T>) => this.mapResultData(result)));
  }

  private mapResultData(result: EditDialogInput<T>): EditDialogResult<T> {
    return {
      context: result.context,
      action: result.action
    };
  }

}
