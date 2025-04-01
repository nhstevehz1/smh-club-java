import {Component, computed, input} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';

import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';

import {BaseEditorComponent} from '@app/shared/components/base-editor';
import {FormControlError} from '@app/shared/components/editor-form-fields';
import {InputFormFieldComponent} from '@app/shared/components/editor-form-fields';

import {Phone} from '@app/features/phones/models/phone';
import {PhoneTypeFormFieldComponent} from '@app/features/phones/phone-type-form-field/phone-type-form-field.component';

@Component({
  selector: 'app-phone-editor',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    InputFormFieldComponent,
    PhoneTypeFormFieldComponent
  ],
  templateUrl: './phone-editor.component.html',
  styleUrl: './phone-editor.component.scss'
})
export class PhoneEditorComponent extends BaseEditorComponent<Phone> {

  countryCode = computed(() => this.editorForm()!.controls.country_code);
  countryCodeErrors = input<FormControlError[]>();

  phoneNumber = computed(() => this.editorForm()!.controls.phone_number);
  phoneNumberErrors= input<FormControlError[]>();

  phoneType= computed(() => this.editorForm()!.controls.phone_type);
  phoneTypeErrors = input<FormControlError[]>();

  constructor() {
    super();
  }
}
