import {EditAction} from './edit-action';

export interface EditDialogResult<T> {
  context : T,
  action: EditAction
}
