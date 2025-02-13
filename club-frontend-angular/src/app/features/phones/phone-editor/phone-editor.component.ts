import {Component, inject, Input, OnInit} from '@angular/core';
import {ControlContainer, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {PhoneType} from "../models/phone-type";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {MatDivider} from "@angular/material/divider";

@Component({
  selector: 'app-phone-editor',
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatSelectModule,
    MatDivider
  ],
  templateUrl: './phone-editor.component.html',
  styleUrl: './phone-editor.component.scss'
})
export class PhoneEditorComponent extends BaseEditorComponent {

  phoneTypes = Object.values(PhoneType);

  public get phoneNumber(): FormControl {
    return this.formGroup.get('phone_number') as FormControl;
  }

  public get phoneType(): FormControl {
    return this.formGroup.get('phone_type') as FormControl;
  }

  constructor(private formBuilder: FormBuilder) {
    super();
  }

  protected createGroup(): FormGroup {
    return this.formBuilder.group({
      country_code: ['1', [Validators.required]],
      phone_number: [null, [Validators.required]],
      phone_type: [PhoneType.Home, [Validators.required]],
    });
  }
}
