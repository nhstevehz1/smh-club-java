import {Component, input, signal} from '@angular/core';
import {AddressType, AddressTypeOption} from "../models/address-type";
import {FormControl} from "@angular/forms";
import {
  SelectFormFieldComponent
} from "../../../shared/components/editor-form-fields/select-form-field/select-form-field.component";
import {MatFormFieldAppearance} from "@angular/material/form-field";

@Component({
  selector: 'app-address-type-form-field',
  imports: [
    SelectFormFieldComponent
  ],
  templateUrl: './address-type-form-field.component.html',
  styleUrl: './address-type-form-field.component.scss'
})
export class AddressTypeFormFieldComponent {

  formControlSignal
      = input.required<FormControl<AddressType>>({alias: 'formControl'});

  appearanceSignal
      = input<MatFormFieldAppearance>(undefined, {alias: 'appearance'});

  optionsSignal
      = signal<Array<AddressTypeOption>> ([])

  labelSignal =
      input<string>(undefined, {alias: 'label'});

  constructor() {
    this.optionsSignal.set([
      {label: 'addresses.type.home', value:AddressType.Home},
      {label: 'addresses.type.work', value:AddressType.Work},
      {label: 'addresses.type.other', value:AddressType.Other}
    ]);
  }
}
