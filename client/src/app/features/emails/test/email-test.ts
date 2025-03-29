import {FormControl, FormGroup} from "@angular/forms";
import {PagedData} from "../../../shared/models";
import {generatePagedData} from "../../../shared/test-helpers/test-helpers";
import {Email, EmailCreate, EmailMember, EmailType} from "../models";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {ColumnDef} from '../../../shared/components/sortable-pageable-table/models/';
import {EditAction, EditDialogInput} from '../../../shared/components/edit-dialog/models';
import {EmailEditorComponent} from '../email-editor/email-editor.component';

export function generateEmailPagedData(page: number, size: number, total: number): PagedData<EmailMember> {
    const content = generateEmailList(size);
    return generatePagedData(page, size, total, content);
}

export function generateEmailList(size: number): EmailMember[] {
    const list: EmailMember[] = [];

    for (let ii = 0; ii < size; ii++) {
        list.push(generateEmailMember(ii));
    }
    return list;
}

export function generateEmailMember(prefix: number): EmailMember {
  return {
    id: prefix,
    member_id: prefix,
    email: `${prefix}email@${prefix}domain.com`,
    email_type: EmailType.Home,
    full_name: {
      first_name: prefix + " First",
      middle_name: prefix +  " Middle",
      last_name: prefix + " Last",
      suffix: prefix + " Suffix"
    }
  }
}

export function generateEmailCreate(): EmailCreate {
  return {
    email: 'test-create@email.com',
    email_type: EmailType.Home
  }
}

export function generateEmail(): Email {
  return {
    id: 0,
    member_id: 0,
    email: 'test-update@email.com',
    email_type: EmailType.Work
  }
}

export function generateEmailForm(): FormModelGroup<Email> {
    return new FormGroup({
      id: new FormControl(0, {nonNullable: true}),
      member_id: new FormControl(0, {nonNullable: true}),
      email: new FormControl('', {nonNullable: true}),
      email_type: new FormControl<EmailType>(EmailType.Home, {nonNullable: true})
    });
}

export function generateEmailDialogInput(context: Email, action: EditAction): EditDialogInput<Email> {
  return {
    title: 'test',
    component: EmailEditorComponent,
    form: generateEmailForm(),
    context: context,
    action: action
  }
}

export function generateEmailColumnDefs(): ColumnDef<EmailMember>[] {
  return [{
    columnName: 'email',
    displayName: 'emails.list.columns.email',
    isSortable: true,
    cell:(element: EmailMember) => `${element.email}`
  }, {
    columnName: 'email_type',
    displayName: 'emails.list.columns.emailType',
    isSortable: false,
    cell:(element: EmailMember) => `${element.email_type}`
  }, {
    columnName: 'full_name',
    displayName: 'emails.list.columns.fullName',
    isSortable: true,
    cell:(element: EmailMember) => `${element.full_name}`
  }
  ];
}
