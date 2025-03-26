import {Component, computed, input} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {PhoneCreate, Phone} from "../models/phone";
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
export class PhoneEditorComponent extends BaseEditorComponent<Phone> {

  countryCode = computed(() => this.editorForm()!.controls.country_code);
  countryCodeErrors = input<FormControlError[]>();

  phoneNumber = computed(() => this.editorForm()!.controls.phone_number);
  phoneNumberErrors= input<FormControlError[]>();

  phoneType= computed(() => this.editorForm()!.controls.phone_type);
  phoneTypeErrors = input<FormControlError[]>();

  constructor() {
    super();
  }
}
