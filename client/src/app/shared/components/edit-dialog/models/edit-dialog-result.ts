import {EditAction} from '@app/shared/components/edit-dialog/models/edit-action';

export interface EditDialogResult<T> {
  context : T,
  action: EditAction
}
