import { Injectable } from '@angular/core';
import {NonNullableFormBuilder} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';

import {FormModelGroup} from '@app/shared/components/base-editor';
import {BaseEditDialogService} from '@app/shared/services/dialog-edit-service';
import {EditAction, EditDialogInput} from '@app/shared/components/edit-dialog';

import {TestEditorComponent, TestModel} from '@app/shared/components/base-table-ex/testing';

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
