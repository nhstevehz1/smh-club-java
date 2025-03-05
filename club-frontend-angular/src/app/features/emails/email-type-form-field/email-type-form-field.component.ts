import {Component, input, signal} from '@angular/core';
import {FormControl} from "@angular/forms";
import {EmailType, EmailTypeOption} from "../models/email-type";
import {
  SelectFormFieldComponent
} from "../../../shared/components/editor-form-fields/select-form-field/select-form-field.component";
import {MatFormFieldAppearance} from "@angular/material/form-field";

@Component({
  selector: 'app-email-type-form-field',
  imports: [
    SelectFormFieldComponent
  ],
  templateUrl: './email-type-form-field.component.html',
  styleUrl: './email-type-form-field.component.scss'
})
export class EmailTypeFormFieldComponent {

  formControlSignal
      = input.required<FormControl<EmailType>>({alias: 'formControl'});

  appearanceSignal
      = input<MatFormFieldAppearance>(undefined, {alias: 'appearance'});

  optionsSignal
      = signal<Array<EmailTypeOption>> ([])

  labelSignal =
      input<string>(undefined, {alias: 'label'});

  constructor() {
    this.optionsSignal.set([
      {label: 'emails.type.home', value: EmailType.Home},
      {label: 'emails.type.work', value: EmailType.Work},
      {label: 'emails.type.other', value: EmailType.Other}
    ]);
  }
}
