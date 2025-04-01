import { Injectable } from '@angular/core';
import {NonNullableFormBuilder} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';

import {BaseEditDialogService} from '@app/shared/services/dialog-edit-service/base-edit-dialog.service';
import {EditAction, EditDialogInput} from '@app/shared/components/edit-dialog/models';

import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {
  TestEditorComponent
} from '@app/shared/components/base-table-component/testing/test-editor/test-editor.component';
import {TestModel} from '@app/shared/components/base-table-component/testing/models/test-models';

@Injectable()
export class TestEditDialogService extends BaseEditDialogService<TestModel>{

  constructor(dialog: MatDialog,
              private fb: NonNullableFormBuilder) {
    super(dialog);
  }

  generateDialogInput(title: string, context: TestModel, action: EditAction): EditDialogInput<TestModel> {
    return {
      title: title,
      component: TestEditorComponent,
      form: this.generateForm(),
      context: context,
      action: action
    }
  }

  generateForm(): FormModelGroup<TestModel> {
    return this.fb.group({
      id: [0],
      test: ['']
    });
  }


}
