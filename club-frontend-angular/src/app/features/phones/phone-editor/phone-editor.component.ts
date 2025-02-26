import {Component, OnInit, signal} from '@angular/core';
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {PhoneType} from "../models/phone-type";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {MatDivider} from "@angular/material/divider";
import {Phone} from "../models/phone";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-phone-editor',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatDivider,
    NgClass
  ],
  templateUrl: './phone-editor.component.html',
  styleUrl: './phone-editor.component.scss'
})
export class PhoneEditorComponent extends BaseEditorComponent<Phone> implements OnInit {

  readonly phoneNumberError = signal(false);
  readonly phoneTypeError = signal(false);

  phoneTypes = Object.values(PhoneType);

  public get phoneNumber(): FormControl {
    return this.editorForm.controls.phone_number;
  }

  public get phoneType(): FormControl {
    return this.editorForm.controls.phone_type;
  }

  constructor() {
    super();
  }

  ngOnInit() {
    this.setErrorSignal(this.phoneNumberError, this.phoneNumber);
    this.setErrorSignal(this.phoneTypeError, this.phoneType);
  }
}
