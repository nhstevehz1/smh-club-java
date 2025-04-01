import {Component, computed, input} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';

import {BaseEditorComponent} from '@app/shared/components/base-editor';
import {FormControlError} from '@app/shared/components/editor-form-fields';
import {InputFormFieldComponent} from '@app/shared/components/editor-form-fields';
import {DateFormFieldComponent} from '@app/shared/components/editor-form-fields';

import {Renewal} from '@app/features/renewals/models/renewal';

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
