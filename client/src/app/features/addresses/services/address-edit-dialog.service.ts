import { Injectable } from '@angular/core';
import {NonNullableFormBuilder, Validators} from '@angular/forms';

import {MatDialog} from '@angular/material/dialog';

import {EditAction, EditDialogInput} from '@app/shared/components/base-edit-dialog/models';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {BaseEditDialogService} from '@app/shared/services/dialog-edit-service/base-edit-dialog.service';

import {Address, AddressType} from '@app/features/addresses/models/address';
import {AddressEditorComponent} from '@app/features/addresses/address-editor/address-editor.component';

@Injectable()
export class AddressEditDialogService extends BaseEditDialogService<Address, AddressEditorComponent> {

  constructor(dialog: MatDialog,
              private fb: NonNullableFormBuilder) {
    super(dialog);
  }

  generateForm(): FormModelGroup<Address> {
    return this.fb.group({
      id: [0],
      member_id: [0],
      address1: ['', [Validators.required]],
      address2: [''],
      city: ['', [Validators.required]],
      state: ['', [Validators.required, Validators.minLength(2)]],
      postal_code: ['', [Validators.required, Validators.minLength(5)]],
      address_type: [AddressType.Home, [Validators.required]]
    });
  }

  generateDialogInput(title: string, context: Address, action: EditAction)
    : EditDialogInput<Address, AddressEditorComponent> {

    return {
      title: title,
      context: context,
      action: action,
      editorConfig: {
        component: AddressEditorComponent,
        form: this.generateForm()
      }
    }
  }
}
