import {Component, input, signal} from '@angular/core';
import {FormControl} from '@angular/forms';

import {MatFormFieldAppearance} from '@angular/material/form-field';

import {EmailType} from '@app/features/emails/models';
import {
  SelectFormFieldComponent
} from '@app/shared/components/editor-form-fields/select-form-field/select-form-field.component';
import {FormControlError, SelectOption} from '@app/shared/components/editor-form-fields/models';

@Component({
  selector: 'app-email-type-form-field',
  imports: [
    SelectFormFieldComponent
  ],
  templateUrl: './email-type-form-field.component.html',
  styleUrl: './email-type-form-field.component.scss'
})
export class EmailTypeFormFieldComponent {

  formControl= input.required<FormControl<EmailType>>();

  appearance= input<MatFormFieldAppearance>();

  label = input<string>();

  controlErrors= input<FormControlError[]>()

  options= signal<SelectOption<EmailType>[]> ([]);

  constructor() {
    this.options.set([
      {label: 'emails.type.home', value: EmailType.Home},
      {label: 'emails.type.work', value: EmailType.Work},
      {label: 'emails.type.other', value: EmailType.Other}
    ]);
  }
}
