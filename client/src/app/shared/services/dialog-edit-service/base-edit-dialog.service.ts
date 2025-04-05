import {MatDialog} from '@angular/material/dialog';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {
  EditAction, EditDialogInput, EditDialogResult
} from '@app/shared/components/base-edit-dialog/models';
import {BaseEditDialogComponent} from '@app/shared/components/base-edit-dialog/base-edit-dialog.component';
import {EditDialogService} from '@app/shared/services/dialog-edit-service/edit-dialog-service';
import {Editor} from '@app/shared/components/base-editor/editor';

export abstract class BaseEditDialogService<T, C extends Editor<T>> implements EditDialogService<T, C>{

  protected constructor(private dialog: MatDialog) { }

  abstract generateDialogInput(title: string, context: T, action: EditAction): EditDialogInput<T, C>;
  abstract generateForm(): FormModelGroup<T>;

  openDialog(dialogInput: EditDialogInput<T, C>): Observable<EditDialogResult<T>> {
    const dialogRef = this.dialog.open<BaseEditDialogComponent<T, C>, EditDialogInput<T, C>>(
      BaseEditDialogComponent<T, C>, {data: dialogInput});

    return dialogRef.afterClosed().pipe(
      map((result: EditDialogInput<T, C>) => this.mapResultData(result)));
  }

  private mapResultData(result: EditDialogInput<T, C>): EditDialogResult<T> {
    return {
      context: result.context,
      action: result.action
    };
  }
}
