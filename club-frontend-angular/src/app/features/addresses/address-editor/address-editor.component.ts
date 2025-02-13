import {Component, OnInit} from '@angular/core';
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AddressType} from "../models/address-type";
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {MatDivider} from "@angular/material/divider";

@Component({
  selector: 'app-address-editor',
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatSelectModule,
    MatDivider
  ],
  templateUrl: './address-editor.component.html',
  styleUrl: './address-editor.component.scss'
})
export class AddressEditorComponent extends BaseEditorComponent implements OnInit{

  addressTypes = Object.values(AddressType);

  public get address1(): FormControl {
    return this.formGroup.get('address1') as FormControl;
  }

  public get address2(): FormControl {
    return this.formGroup.get('address2') as FormControl;
  }

  public get city(): FormControl {
    return this.formGroup.get('city') as FormControl;
  }

  public get state(): FormControl {
    return this.formGroup.get('state') as FormControl;
  }

  public get zip(): FormControl {
    return this.formGroup.get('zip') as FormControl;
  }

  public get addressType(): FormControl {
    return this.formGroup.get('address_type') as FormControl;
  }

  constructor(private formBuilder: FormBuilder) {
    super();
  }

  protected createGroup(): FormGroup {
    return this.formBuilder.group({
      address1: [null, [Validators.required]],
      address2: [null],
      city: [null, [Validators.required]],
      state: [null, [Validators.required, Validators.minLength(2)]],
      zip: [null, [Validators.required, Validators.minLength(5)]],
      address_type: [AddressType.Home, [Validators.required]],
    });
  }
}
