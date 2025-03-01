import {Component, OnInit, Signal, signal, WritableSignal} from '@angular/core';
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {AddressType} from "../models/address-type";
import {BaseEditorComponent} from "../../../shared/components/base-editor/base-editor.component";
import {Address} from "../models/address";
import {NgClass} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-address-editor',
    imports: [
        ReactiveFormsModule,
        MatButtonModule,
        MatIconModule,
        MatInputModule,
        MatSelectModule,
        NgClass,
        TranslatePipe
    ],
  templateUrl: './address-editor.component.html',
  styleUrl: './address-editor.component.scss'
})
export class AddressEditorComponent extends BaseEditorComponent<Address> implements OnInit{

  addressTypes = Object.values(AddressType);

  readonly showTitle: Signal<boolean> = signal(!!this.title);

  readonly address1Error: WritableSignal<boolean> = signal(false);
  readonly address2Error: WritableSignal<boolean> = signal(false);
  readonly cityError: WritableSignal<boolean> = signal(false);
  readonly stateError: WritableSignal<boolean> = signal(false);
  readonly zipError: WritableSignal<boolean> = signal(false);
  readonly addressTypeError: WritableSignal<boolean> = signal(false);

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

  ngOnInit() {

    this.setErrorSignal(this.address1Error, this.address1);
    this.setErrorSignal(this.address2Error, this.address2);
    this.setErrorSignal(this.cityError, this.city);
    this.setErrorSignal(this.stateError, this.state);
    this.setErrorSignal(this.zipError, this.zip);
    this.setErrorSignal(this.addressTypeError, this.addressType);
  }
}
