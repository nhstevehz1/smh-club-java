import {Component, input, signal} from '@angular/core';
import {AddressType, AddressTypeOption} from "../models/address-type";
import {FormControl} from "@angular/forms";
import {
  SelectFormFieldComponent
} from "../../../shared/components/editor-form-fields/select-form-field/select-form-field.component";
import {MatFormFieldAppearance} from "@angular/material/form-field";
import {FormControlError} from "../../../shared/components/editor-form-fields/models/form-control-error";

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

  options= signal<AddressTypeOption[]>([]);

  constructor() {
    this.options.set([
      {label: 'addresses.type.home', value:AddressType.Home},
      {label: 'addresses.type.work', value:AddressType.Work},
      {label: 'addresses.type.other', value:AddressType.Other}
    ]);
  }
}
