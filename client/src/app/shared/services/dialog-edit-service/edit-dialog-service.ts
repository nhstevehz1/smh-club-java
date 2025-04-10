import {Observable} from 'rxjs';
import {EditAction, EditDialogInput, EditDialogResult} from '@app/shared/components/base-edit-dialog/models';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {Editor} from '@app/shared/components/base-editor/editor';

export interface EditDialogService<T, C extends Editor<T>> {
  openDialog(dialogData: EditDialogInput<T, C>): Observable<EditDialogResult<T>>;
  generateForm(): FormModelGroup<T>;
  generateDialogInput(title: string, context: T, action: EditAction): EditDialogInput<T, C>;
}
