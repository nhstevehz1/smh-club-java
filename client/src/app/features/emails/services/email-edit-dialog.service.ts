import {Injectable} from '@angular/core';
import {NonNullableFormBuilder, Validators} from '@angular/forms';

import {MatDialog} from '@angular/material/dialog';

import {FormModelGroup} from '@app/shared/components/base-editor';
import {EditAction, EditDialogInput} from '@app/shared/components/edit-dialog';
import {BaseEditDialogService} from '@app/shared/services/dialog-edit-service';

import {Email, EmailType} from '@app/features/emails/models/email';
import {EmailEditorComponent} from '@app/features/emails/email-editor/email-editor.component';

@Injectable()
export class EmailEditDialogService extends BaseEditDialogService<Email> {
  constructor(dialog: MatDialog,
              private fb: NonNullableFormBuilder) {
    super(dialog);
  }

  generateForm(): FormModelGroup<Email> {
    return this.fb.group({
      id: [0],
      member_id: [0],
      email: ['', [Validators.required, Validators.email]],
      email_type: [EmailType.Home, [Validators.required]]
    });
  }

  generateDialogInput(title: string, context: Email, action: EditAction): EditDialogInput<Email> {
    return {
      title: title,
      component: EmailEditorComponent,
      form: this.generateForm(),
      context: context,
      action: action
    }
  }
}
