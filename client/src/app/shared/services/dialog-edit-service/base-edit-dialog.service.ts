import {MatDialog} from '@angular/material/dialog';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {
  EditAction, EditDialogInput, EditDialogResult
} from '@app/shared/components/edit-dialog/models';
import {EditDialogComponent} from '@app/shared/components/edit-dialog/edit-dialog.component';
import {EditDialogService} from '@app/shared/services/dialog-edit-service/edit-dialog-service';

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
