import {Component, computed} from '@angular/core';
import {EditorModel} from '@app/shared/components/base-editor/testing/models/EditorModel';
import {
  InputFormFieldComponent
} from '@app/shared/components/editor-form-fields/input-form-field/input-form-field.component';
import {ReactiveFormsModule} from '@angular/forms';
import {BaseEditorExComponent} from '@app/shared/components/base-editor/base-editor-ex.component';

@Component({
  selector: 'app-mock-editor2',
  imports: [
    InputFormFieldComponent,
    ReactiveFormsModule
  ],
  templateUrl: './mock-editor2.component.html',
  styleUrl: './mock-editor2.component.scss'
})
export class MockEditor2Component extends BaseEditorExComponent<EditorModel>{
  field = computed(() => this.editorForm()!.controls.editorField);
}
