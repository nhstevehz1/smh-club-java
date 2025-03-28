import {Component, input, signal} from '@angular/core';
import {FormControl} from "@angular/forms";
import {EmailType} from "../models/email-type";
import {
  SelectFormFieldComponent
} from "../../../shared/components/editor-form-fields/select-form-field/select-form-field.component";
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {FormControlError} from "../../../shared/components/editor-form-fields/models/form-control-error";
import {SelectOption} from '../../../shared/components/editor-form-fields/models/select-option';

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
