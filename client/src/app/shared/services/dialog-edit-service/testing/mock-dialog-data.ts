import {EditAction, EditDialogInput} from '../../models';
import {FormModelGroup} from '../../../base-editor/form-model-group';
import {FormControl, FormGroup} from '@angular/forms';

export interface MockModel {
  id: number,
  test: string,
}

export class MockEditorComponent{}

export function generateMockModel(): MockModel {
  return {
    id: 0,
    test: 'test'
  }
}

export function generateMockForm(): FormModelGroup<MockModel> {
  return new FormGroup({
    id: new FormControl(0, {nonNullable: true}),
    test: new FormControl('', {nonNullable: true})
  });
}

export function generateDialogInput(): EditDialogInput<MockModel> {
  return {
    title: 'test',
    component: MockEditorComponent,
    form: generateMockForm(),
    context: generateMockModel(),
    action: EditAction.Cancel
  }
}
