import {FormModelGroup} from '@app/shared/components/base-editor';
import {EditAction} from '@app/shared/components/edit-dialog';


export interface EditDialogInput<T> {
  title: string,
  context: T,
  form: FormModelGroup<T>,
  component: any,
  action: EditAction
}
