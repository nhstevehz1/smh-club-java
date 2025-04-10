import {EditAction} from '@app/shared/components/base-edit-dialog/models/edit-action';

export interface EditDialogResult<T> {
  context : T,
  action: EditAction
}
