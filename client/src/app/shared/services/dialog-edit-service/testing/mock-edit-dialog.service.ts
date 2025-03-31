import {Injectable} from "@angular/core";
import {NonNullableFormBuilder} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';

import {BaseEditDialogService} from '@app/shared/services';
import {EditAction, EditDialogInput} from '@app/shared/components/edit-dialog';
import {MockModel} from '@app/shared/services/api-service/testing';
import {MockEditorComponent} from '@app/shared/services/dialog-edit-service/testing/mock-dialog-data';
import {FormModelGroup} from '@app/shared/components/base-editor';


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
