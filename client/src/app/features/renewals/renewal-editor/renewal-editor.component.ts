import {Component, computed, input} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {BaseEditorComponent} from '../../../shared/components/base-editor/base-editor.component';
import {Renewal} from '../models/renewal';
import {FormControlError} from '../../../shared/components/editor-form-fields/models/form-control-error';
import {
  InputFormFieldComponent
} from '../../../shared/components/editor-form-fields/input-form-field/input-form-field.component';
import {
  DateFormFieldComponent
} from '../../../shared/components/editor-form-fields/date-form-field/date-form-field.component';

@Component({
  selector: 'app-renewal-editor',
  imports: [
    ReactiveFormsModule,
    InputFormFieldComponent,
    DateFormFieldComponent
  ],
  templateUrl: './renewal-editor.component.html',
  styleUrl: './renewal-editor.component.scss'
})
export class RenewalEditorComponent extends BaseEditorComponent<Renewal>{
  renewalDate = computed(() => this.editorForm()!.controls.renewal_date);
  renewalDateErrors = input<FormControlError[]>();

  renewalYear = computed(() => this.editorForm()!.controls.renewal_year);
  renewalYearErrors = input<FormControlError[]>();
}
