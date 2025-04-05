import {Signal, WritableSignal} from '@angular/core';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {EditorConfig} from '@app/shared/components/base-editor/models/editor-config';
import {Editor} from '@app/shared/components/base-editor/editor';
import {EditDialogInput} from '@app/shared/components/base-edit-dialog/models';

export interface EditDialog<T, C extends Editor<T>> {
  editForm: Signal<FormModelGroup<T>>;
  title: Signal<string>;
  isDeleteAction: Signal<boolean>;
  editorConfig: Signal<EditorConfig<T, C>>

  dialogInput: WritableSignal<EditDialogInput<T, C>>

}
