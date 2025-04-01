import {EditAction} from '@app/shared/components/edit-dialog/models/edit-action';
import {FormModelGroup} from '@app/shared/components/base-editor/models';

export interface EditDialogInput<T> {
  title: string,
  context: T,
  form: FormModelGroup<T>,
  component: any,
  action: EditAction
}
