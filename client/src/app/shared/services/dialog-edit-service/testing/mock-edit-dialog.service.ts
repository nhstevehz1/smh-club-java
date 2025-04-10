import {Injectable} from '@angular/core';
import {NonNullableFormBuilder} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';

import {BaseEditDialogService} from '@app/shared/services/dialog-edit-service/base-edit-dialog.service';
import {EditAction, EditDialogInput} from '@app/shared/components/base-edit-dialog/models';
import {
  DialogServiceModel,
  MockServiceEditorComponent
} from '@app/shared/services/dialog-edit-service/testing/test-support';
import {FormModelGroup} from '@app/shared/components/base-editor/models';

@Injectable()
export class MockEditDialogService extends BaseEditDialogService<DialogServiceModel, MockServiceEditorComponent> {
  constructor(dialog: MatDialog, private fb: NonNullableFormBuilder) {
    super(dialog);
  }

  generateDialogInput(title: string,
                      context: DialogServiceModel,
                      action: EditAction): EditDialogInput<DialogServiceModel, MockServiceEditorComponent> {
    return {
      title: title,
      context: context,
      action: action,
      editorConfig: {
        component: MockServiceEditorComponent,
        form: this.generateForm()
      }
    }
  }

  generateForm(): FormModelGroup<DialogServiceModel> {
    return this.fb.group({
      id: [0],
      dialogServiceField: ['']
    })
  }

}
