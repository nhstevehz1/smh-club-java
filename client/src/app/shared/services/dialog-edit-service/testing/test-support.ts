import {FormControl, FormGroup} from '@angular/forms';

import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {EditDialogInput, EditAction, EditDialogResult} from '@app/shared/components/base-edit-dialog/models';
import {BaseEditorComponent} from '@app/shared/components/base-editor/base-editor.component';
import {BaseEditDialogComponent} from '@app/shared/components/base-edit-dialog/base-edit-dialog.component';

export interface DialogServiceModel {
  id: number,
  dialogServiceField: string
}

export class DialogServiceTest {
  static generateDialogServiceModel(): DialogServiceModel {
    return {
      id: 0,
      dialogServiceField: 'test'
    }
  }

  static generateDialogInput(): EditDialogInput<DialogServiceModel, MockServiceEditorComponent> {
    return {
      title: 'test',
      context: this.generateDialogServiceModel(),
      action: EditAction.Cancel,
      editorConfig: {
        component: MockServiceEditorComponent,
        form: this.generateDialogServiceModelForm()
      }
    }
  }

  static generateDialogResult(): EditDialogResult<DialogServiceModel> {
    return {
      context: this.generateDialogServiceModel(),
      action: EditAction.Cancel
    }
  }

  static generateDialogServiceModelForm(): FormModelGroup<DialogServiceModel> {
    return new FormGroup({
      id: new FormControl(0, {nonNullable: true}),
      dialogServiceField: new FormControl('', {nonNullable: true})
    });
  }
}

export class MockServiceEditorComponent
  extends BaseEditorComponent<DialogServiceModel>{}

export class MockServiceDialogComponent
  extends BaseEditDialogComponent<DialogServiceModel, MockServiceEditorComponent>{}
