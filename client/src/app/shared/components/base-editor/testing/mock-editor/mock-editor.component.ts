import {Component, computed} from '@angular/core';
import {EditorModel} from '@app/shared/components/base-editor/testing/models/EditorModel';
import {
  InputFormFieldComponent
} from '@app/shared/components/editor-form-fields/input-form-field/input-form-field.component';
import {ReactiveFormsModule} from '@angular/forms';
import {BaseEditorComponent} from '@app/shared/components/base-editor/base-editor.component';

@Component({
  selector: 'app-mock-editor',
  imports: [
    InputFormFieldComponent,
    ReactiveFormsModule
  ],
  templateUrl: './mock-editor.component.html'
})
export class MockEditorComponent extends BaseEditorComponent<EditorModel>{
  field = computed(() => this.editorForm()!.controls.editorField);
}
