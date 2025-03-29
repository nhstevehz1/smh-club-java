import {Injectable} from "@angular/core";
import {BaseEditDialogService} from "../base-edit-dialog.service";
import {MockEditorComponent, MockModel} from './mock-dilog-data';
import {EditAction, EditDialogInput} from '../../models';
import {FormModelGroup} from '../../../base-editor/form-model-group';
import {MatDialog} from '@angular/material/dialog';
import {NonNullableFormBuilder} from '@angular/forms';

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
