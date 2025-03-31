import {Component, computed, input} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';

import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';

import {InputFormFieldComponent} from '@app/shared/components/editor-form-fields';
import {FormControlError} from '@app/shared/components/editor-form-fields/models/form-control-error';
import {BaseEditorComponent} from '@app/shared/components/base-editor';

import {Address, AddressTypeFormFieldComponent} from '@app/features/addresses';

@Component({
  selector: 'app-address-editor',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    InputFormFieldComponent,
    AddressTypeFormFieldComponent
  ],
  templateUrl: './address-editor.component.html',
  styleUrl: './address-editor.component.scss'
})
export class AddressEditorComponent extends BaseEditorComponent<Address> {

  address1 = computed(() => this.editorForm()!.controls.address1);
  address1Errors = input<FormControlError[]>();

  address2 = computed(() => this.editorForm()!.controls.address2);
  address2Errors= input<FormControlError[]>();

  city = computed(() => this.editorForm()!.controls.city);
  cityErrors = input<FormControlError[]>();

  state = computed(() => this.editorForm()!.controls.state);
  stateErrors = input<FormControlError[]>();

  postalCode = computed(() => this.editorForm()!.controls.postal_code);
  postalCodeErrors = input<FormControlError[]>();

  addressType = computed(() => this.editorForm()!.controls.address_type);
  addressTypeErrors = input<FormControlError[]>();

  constructor() {
    super();
  }
}
