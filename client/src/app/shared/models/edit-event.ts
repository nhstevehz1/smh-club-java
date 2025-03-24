import {FormModelGroup} from '../components/base-editor/form-model-group';

export interface EditEvent<T> {
  idx: number,
  data: T
}

export interface EditDialogData<T> {
  data: T,
  form?: FormModelGroup<T>
  action: EditAction
}

export enum EditAction {
  Edit,
  Create,
  Delete,
  Cancel
}
