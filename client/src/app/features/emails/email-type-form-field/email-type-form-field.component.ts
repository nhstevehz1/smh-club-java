import {Component, input, signal} from '@angular/core';
import {FormControl} from '@angular/forms';

import {MatFormFieldAppearance} from '@angular/material/form-field';

import {
  SelectFormFieldComponent, SelectOption, FormControlError
} from '@app/shared/components/editor-form-fields';

import {EmailType} from '@app/features/emails/models/email';

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
