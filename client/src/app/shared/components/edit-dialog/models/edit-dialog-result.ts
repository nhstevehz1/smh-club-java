import {EditAction} from '@app/shared/components/edit-dialog';

export interface EditDialogResult<T> {
  context : T,
  action: EditAction
}
