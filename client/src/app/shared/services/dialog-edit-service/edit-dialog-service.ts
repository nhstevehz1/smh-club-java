import {Observable} from 'rxjs';

import {EditAction, EditDialogInput, EditDialogResult} from '@app/shared/components/edit-dialog';
import {FormModelGroup} from '@app/shared/components/base-editor';

export interface EditDialogService<T> {
  openDialog(dialogData: EditDialogInput<T>): Observable<EditDialogResult<T>>;
  generateForm(): FormModelGroup<T>;
  generateDialogInput(title: string, context: T, action: EditAction): EditDialogInput<T>;
}
