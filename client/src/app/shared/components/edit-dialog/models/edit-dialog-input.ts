import {FormModelGroup} from '../../base-editor/form-model-group';
import {EditAction} from './edit-action';

export interface EditDialogInput<T> {
  title: string,
  context: T,
  form: FormModelGroup<T>,
  component: any,
  action: EditAction
}
