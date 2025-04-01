import { Injectable } from '@angular/core';
import {NonNullableFormBuilder, Validators} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';
import {DateTime} from 'luxon';

import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {EditAction, EditDialogInput} from '@app/shared/components/edit-dialog/models';
import {BaseEditDialogService} from '@app/shared/services/dialog-edit-service/base-edit-dialog.service';

import {Renewal} from '@app/features/renewals/models/renewal';
import {RenewalEditorComponent} from '@app/features/renewals/renewal-editor/renewal-editor.component';

@Injectable()
export class RenewalEditDialogService extends BaseEditDialogService<Renewal>{

  constructor(dialog: MatDialog,
              private fb: NonNullableFormBuilder) {
    super(dialog);
  }

  generateForm(): FormModelGroup<Renewal> {
    return this.fb.group({
      id: [0],
      member_id: [0],
      renewal_date: [DateTime.now(), [Validators.required]],
      renewal_year: [DateTime.now().year, [Validators.required]]
    });
  }

  generateDialogInput(title: string, context: Renewal, action: EditAction): EditDialogInput<Renewal> {
    return {
      title: title,
      component: RenewalEditorComponent,
      form: this.generateForm(),
      context: context,
      action: action
    }
  }
}
