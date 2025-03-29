import {FormModelGroup} from '../../base-editor/form-model-group';
import {EditAction, EditDialogInput, EditDialogResult} from '../models';
import {Observable} from 'rxjs';

export interface EditDialogService<T> {
  openDialog(dialogData: EditDialogInput<T>): Observable<EditDialogResult<T>>;
  generateForm(): FormModelGroup<T>;
  generateDialogInput(title: string, context: T, action: EditAction): EditDialogInput<T>;
}
