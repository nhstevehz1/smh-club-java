import {MatDialog} from '@angular/material/dialog';
import {EditAction, EditDialogInput, EditDialogResult} from '../models';
import {EditDialogComponent} from '../edit-dialog.component';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {EditDialogService} from './edit-dialog-service';
import {FormModelGroup} from '../../base-editor/form-model-group';

export abstract class BaseEditDialogService<T> implements EditDialogService<T>{

  protected constructor(private dialog: MatDialog) { }

  abstract generateDialogInput(title: string, context: T, action: EditAction): EditDialogInput<T>;
  abstract generateForm(): FormModelGroup<T>;

  openDialog(dialogInput: EditDialogInput<T>): Observable<EditDialogResult<T>> {
    const dialogRef = this.dialog.open<EditDialogComponent<T>, EditDialogInput<T>>(
      EditDialogComponent<T>, {data: dialogInput});

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
