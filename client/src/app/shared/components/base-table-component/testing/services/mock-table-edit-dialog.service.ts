import { Injectable } from '@angular/core';
import {NonNullableFormBuilder} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';

import {BaseEditDialogService} from '@app/shared/services/dialog-edit-service/base-edit-dialog.service';
import {EditAction, EditDialogInput} from '@app/shared/components/base-edit-dialog/models';

import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {TableModel} from '@app/shared/components/base-table-component/testing/models/test-models';
import {MockTableEditorComponent} from '@app/shared/components/base-table-component/testing/mock-editor/mock-table-editor.component';

@Injectable()
export class MockTableEditDialogService extends BaseEditDialogService<TableModel, MockTableEditorComponent>{

  constructor(dialog: MatDialog,
              private fb: NonNullableFormBuilder) {
    super(dialog);
  }

  generateDialogInput(title: string, context: TableModel, action: EditAction): EditDialogInput<TableModel, MockTableEditorComponent> {
    return {
      title: title,
      context: context,
      action: action,
      editorConfig: {
        component: MockTableEditorComponent,
        form: this.generateForm()
      }
    }
  }

  generateForm(): FormModelGroup<TableModel> {
    return this.fb.group({
      id: [0],
      tableField: ['']
    });
  }
}
