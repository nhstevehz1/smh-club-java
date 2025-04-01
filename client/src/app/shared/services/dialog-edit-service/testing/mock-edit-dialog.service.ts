import {Injectable} from '@angular/core';
import {NonNullableFormBuilder} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';

import {BaseEditDialogService} from '@app/shared/services/dialog-edit-service/base-edit-dialog.service';
import {EditAction, EditDialogInput} from '@app/shared/components/edit-dialog/models';
import {MockModel} from '@app/shared/services/api-service/testing/mock-api-data';
import {MockEditorComponent} from '@app/shared/services/dialog-edit-service/testing/mock-dialog-data';
import {FormModelGroup} from '@app/shared/components/base-editor/models';

@Injectable()
export class MockEditDialogService extends BaseEditDialogService<MockModel> {
  constructor(dialog: MatDialog, private fb: NonNullableFormBuilder) {
    super(dialog);
  }

  generateDialogInput(title: string, context: MockModel, action: EditAction): EditDialogInput<MockModel> {
    return {
      title: title,
      component: MockEditorComponent,
      form: this.generateForm(),
      context: context,
      action: action
    }
  }

  generateForm(): FormModelGroup<MockModel> {
    return this.fb.group({
      id: [0],
      test: ['']
    })
  }

}
