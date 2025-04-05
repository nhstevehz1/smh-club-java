import {Component, computed} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {
  InputFormFieldComponent
} from '@app/shared/components/editor-form-fields/input-form-field/input-form-field.component';
import {BaseEditorComponent} from '@app/shared/components/base-editor/base-editor.component';
import {EditDialogModel} from '@app/shared/components/base-edit-dialog/testing/test-support';

@Component({
  selector: 'app-mock-dialog-editor',
  imports: [
    FormsModule,
    InputFormFieldComponent,
    ReactiveFormsModule
  ],
  templateUrl: './mock-dialog-editor.component.html'
})
export class MockDialogEditorComponent extends BaseEditorComponent<EditDialogModel> {
  dialogField = computed(() => this.editorForm()!.controls.dialogField);
}
