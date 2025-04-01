import {Component, input, signal} from '@angular/core';
import {FormControl} from '@angular/forms';

import {MatFormFieldAppearance} from '@angular/material/form-field';

import {SelectFormFieldComponent} from '@app/shared/components/editor-form-fields';
import {FormControlError} from '@app/shared/components/editor-form-fields';
import {SelectOption} from '@app/shared/components/editor-form-fields';

import {AddressType} from '@app/features/addresses/models/address-models';

@Component({
  selector: 'app-address-type-form-field',
  imports: [
    SelectFormFieldComponent
  ],
  templateUrl: './address-type-form-field.component.html',
  styleUrl: './address-type-form-field.component.scss'
})
export class AddressTypeFormFieldComponent {

  formControl= input.required<FormControl<AddressType>>();

  appearance= input<MatFormFieldAppearance>();

  label = input<string>();

  controlErrors = input<FormControlError[]>();

  options= signal<SelectOption<AddressType>[]>([]);

  constructor() {
    this.options.set([
      {label: 'addresses.type.home', value:AddressType.Home},
      {label: 'addresses.type.work', value:AddressType.Work},
      {label: 'addresses.type.other', value:AddressType.Other}
    ]);
  }
}
