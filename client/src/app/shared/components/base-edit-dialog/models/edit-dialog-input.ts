import {EditAction} from '@app/shared/components/base-edit-dialog/models/edit-action';
import {EditorConfig} from '@app/shared/components/base-editor/models/editor-config';
import {Editor} from '@app/shared/components/base-editor/editor';

export interface EditDialogInput<T, C extends Editor<T>> {
  title: string,
  context: T,
  action: EditAction
  editorConfig: EditorConfig<T, C>
}
