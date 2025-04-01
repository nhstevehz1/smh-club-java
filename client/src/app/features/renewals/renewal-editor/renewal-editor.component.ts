import {Component, computed, input} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';

import {FormControlError} from '@app/shared/components/editor-form-fields/models';
import {Renewal} from '@app/features/renewals/models/renewal';
import {
  InputFormFieldComponent
} from '@app/shared/components/editor-form-fields/input-form-field/input-form-field.component';
import {BaseEditorComponent} from '@app/shared/components/base-editor/base-editor.component';
import {
  DateFormFieldComponent
} from '@app/shared/components/editor-form-fields/date-form-field/date-form-field.component';

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
