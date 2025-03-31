import { Injectable } from '@angular/core';
import {NonNullableFormBuilder, Validators} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';

import {DateTime} from 'luxon';

import {FormModelGroup} from '@app/shared/components/base-editor';
import {BaseEditDialogService} from '@app/shared/services';
import {EditAction, EditDialogInput} from '@app/shared/components/edit-dialog';

import {MemberEditorComponent, Member} from '@app/features/members';

@Injectable()
export class MemberEditDialogService extends BaseEditDialogService<Member> {

  constructor(dialog: MatDialog,
              private fb: NonNullableFormBuilder) {
    super(dialog);
  }

  generateForm(): FormModelGroup<Member> {
    return this.fb.group({
      id: [0],
      member_number: [0, [Validators.required, Validators.min(1)]],
      first_name: ['', [Validators.required]],
      middle_name: [''],
      last_name: ['', [Validators.required]],
      suffix: [''],
      birth_date: [DateTime.now(), [Validators.required]],
      joined_date: [DateTime.now(), [Validators.required]],
    });
  }

  generateDialogInput(title: string, context: Member, action: EditAction): EditDialogInput<Member> {
    return {
      title: title,
      component: MemberEditorComponent,
      form: this.generateForm(),
      context: context,
      action: action
    }
  }
}
