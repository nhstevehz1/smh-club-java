import {Component} from '@angular/core';
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {AddressType} from "../models/address-type";
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {Address} from "../models/address";
import {NgClass} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'app-address-editor',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    NgClass
  ],
  templateUrl: './address-editor.component.html',
  styleUrl: './address-editor.component.scss'
})
export class AddressEditorComponent extends BaseEditorComponent<Address> {

  addressTypes = Object.values(AddressType);

  public get address1(): FormControl {
    return this.editorForm.controls.address1;
  }

  public get address2(): FormControl {
    return this.editorForm.controls.address2;
  }

  public get city(): FormControl {
    return this.editorForm.controls.city;
  }

  public get state(): FormControl {
    return this.editorForm.controls.state;
  }

  public get zip(): FormControl {
    return this.editorForm.controls.zip;
  }

  public get addressType(): FormControl {
    return this.editorForm.controls.address_type;
  }

  constructor() {
    super();
  }

}
