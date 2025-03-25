import {FormModelGroup} from '../../base-editor/form-model-group';

export interface EditEvent<T> {
  idx: number,
  data: T
}

export interface EditDialogData<T> {
  title: string,
  context: T,
  form: FormModelGroup<T>,
  component: any,
  action: EditAction
}

export enum EditAction {
  Edit,
  Create,
  Delete,
  Cancel
}
