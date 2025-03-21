import {Component, computed, input} from '@angular/core';
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {ReactiveFormsModule} from "@angular/forms";
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {AddressCreate} from "../models/address";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {EditorHeaderComponent} from "../../../shared/components/editor-header/editor-header.component";
import {
  InputFormFieldComponent
} from "../../../shared/components/editor-form-fields/input-form-field/input-form-field.component";
import {AddressTypeFormFieldComponent} from "../address-type-form-field/address-type-form-field.component";
import {FormControlError} from "../../../shared/components/editor-form-fields/models/form-control-error";

@Component({
  selector: 'app-address-editor',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    EditorHeaderComponent,
    InputFormFieldComponent,
    AddressTypeFormFieldComponent
  ],
  templateUrl: './address-editor.component.html',
  styleUrl: './address-editor.component.scss'
})
export class AddressEditorComponent extends BaseEditorComponent<AddressCreate> {

  address1Signal = computed(() => this.editorFormSignal().controls.address1);
  address1ErrorsSignal
      = input<FormControlError[]>(undefined, {alias: 'address1Errors'});

  address2Signal = computed(() => this.editorFormSignal().controls.address2);
  address2ErrorsSignal
      = input<FormControlError[]>(undefined, {alias: 'address2Errors'});

  citySignal = computed(() => this.editorFormSignal().controls.city);
  cityErrorsSignal
      = input<FormControlError[]>(undefined, {alias: 'cityErrors'});

  stateSignal = computed(() => this.editorFormSignal().controls.state);
  stateErrorsSignal
      = input<FormControlError[]>(undefined, {alias: 'stateErrors'});

  postalCodeSignal = computed(() => this.editorFormSignal().controls.postal_code);
  postalCodeErrorsSignal
      = input<FormControlError[]>(undefined, {alias: 'postalCodeErrors'});

  addressTypeSignal = computed(() => this.editorFormSignal().controls.address_type);
  addressTypeErrorsSignal
      = input<FormControlError[]>(undefined, {alias: 'addressTypeErrors'});

  constructor() {
    super();
  }
}
