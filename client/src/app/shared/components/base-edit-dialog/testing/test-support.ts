import {EditDialogInput, EditAction} from '@app/shared/components/base-edit-dialog/models';
import {
  MockDialogEditorComponent
} from '@app/shared/components/base-edit-dialog/testing/mock-dialog-editor/mock-dialog-editor.component';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {FormGroup, FormControl} from '@angular/forms';

export interface EditDialogModel {
  id: number,
  dialogField: string
}

export class DialogTest {
  static generateEditDialogModel(): EditDialogModel {
    return {
      id: 10,
      dialogField: 'dialog field'
    }
  }

  static generateEditDialogForm(): FormModelGroup<EditDialogModel> {
    return new FormGroup({
      id: new FormControl(0, {nonNullable: true}),
      dialogField: new FormControl('', {nonNullable: true})
    });
  }
  static generatedEditDialogInput(model: EditDialogModel, action: EditAction)
    : EditDialogInput<EditDialogModel, MockDialogEditorComponent> {

    return {
      title: 'title',
      context: model,
      action: action,
      editorConfig: {
        component: MockDialogEditorComponent,
        form: DialogTest.generateEditDialogForm()
      }
    }
  }
}
