import {Component, computed, input} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {PhoneCreate, PhoneUpdate} from "../models/phone";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {EditorHeaderComponent} from "../../../shared/components/editor-header/editor-header.component";
import {
  InputFormFieldComponent
} from "../../../shared/components/editor-form-fields/input-form-field/input-form-field.component";
import {PhoneTypeFormFieldComponent} from "../phone-type-form-field/phone-type-form-field.component";
import {FormControlError} from "../../../shared/components/editor-form-fields/models/form-control-error";

@Component({
  selector: 'app-phone-editor',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    EditorHeaderComponent,
    InputFormFieldComponent,
    PhoneTypeFormFieldComponent
  ],
  templateUrl: './phone-editor.component.html',
  styleUrl: './phone-editor.component.scss'
})
export class PhoneEditorComponent extends BaseEditorComponent<PhoneCreate | PhoneUpdate> {

  countryCodeSignal
      = computed(() => this.editorFormSignal().controls.country_code);
  countryCodeErrorsSignal
      = input<Array<FormControlError>>(undefined, {alias: 'countryCodeErrors'});

  phoneNumberSignal
      = computed(() => this.editorFormSignal().controls.phone_number);
  phoneNumberErrorsSignal
      = input<Array<FormControlError>>(undefined, {alias: 'phoneNumberErrors'});

  phoneTypeSignal
      = computed(() => this.editorFormSignal().controls.phone_type);
  phoneTypeErrorsSignal
      = input<Array<FormControlError>>(undefined, {alias: 'phoneTypeErrors'});

  constructor() {
    super();
  }
}
