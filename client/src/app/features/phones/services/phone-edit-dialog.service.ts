import { Injectable } from '@angular/core';
import {NonNullableFormBuilder, Validators} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';

import {EditAction, EditDialogInput} from '@app/shared/components/base-edit-dialog/models';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {BaseEditDialogService} from '@app/shared/services/dialog-edit-service/base-edit-dialog.service';

import {Phone, PhoneType} from '@app/features/phones/models/phone';
import {PhoneEditorComponent} from '@app/features/phones/phone-editor/phone-editor.component';

@Injectable()
export class PhoneEditDialogService extends BaseEditDialogService<Phone, PhoneEditorComponent> {

  constructor(dialog: MatDialog,
              private fb: NonNullableFormBuilder) {
    super(dialog);
  }

  generateDialogInput(title: string, context: Phone, action: EditAction)
    : EditDialogInput<Phone, PhoneEditorComponent> {

    return {
      title: title,
      context: context,
      action: action,
      editorConfig: {
        component: PhoneEditorComponent,
        form: this.generateForm()
      }
    };
  }

  generateForm(): FormModelGroup<Phone> {
    return this.fb.group({
      id: [0],
      member_id: [0],
      country_code: ['', [Validators.required]],
      phone_number: ['', [Validators.required]],
      phone_type: [PhoneType.Home, [Validators.required]],
    });
  }


}
