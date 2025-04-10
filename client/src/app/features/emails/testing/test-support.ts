import {FormControl, FormGroup} from '@angular/forms';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';
import {EditAction, EditDialogInput} from '@app/shared/components/base-edit-dialog/models';
import {PagedData} from '@app/shared/services/api-service/models';
import {EmailMember, EmailType, EmailCreate, Email} from '@app/features/emails/models/email';
import {EmailEditorComponent} from '@app/features/emails/email-editor/email-editor.component';
import {TestHelpers} from '@app/shared/testing';


export class EmailTest {
  static generatePagedData(page: number, size: number, total: number): PagedData<EmailMember> {
    const content = this.generateList(size);
    return TestHelpers.generatePagedData(page, size, total, content);
  }

  static generateList(size: number): EmailMember[] {
    const list: EmailMember[] = [];

    for (let ii = 0; ii < size; ii++) {
      list.push(this.generateEmailMember(ii));
    }
    return list;
  }

  static generateEmailMember(prefix: number): EmailMember {
    return {
      id: prefix,
      member_id: prefix,
      email: `${prefix}email@${prefix}domain.com`,
      email_type: EmailType.Home,
      full_name: {
        first_name: prefix + ' First',
        middle_name: prefix +  ' Middle',
        last_name: prefix + ' Last',
        suffix: prefix + ' Suffix'
      }
    }
  }

  static generateEmailCreate(): EmailCreate {
    return {
      email: 'test-create@email.com',
      email_type: EmailType.Home
    }
  }

  static generateEmail(): Email {
    return {
      id: 0,
      member_id: 0,
      email: 'test-update@email.com',
      email_type: EmailType.Work
    }
  }

  static generateForm(): FormModelGroup<Email> {
    return new FormGroup({
      id: new FormControl(0, {nonNullable: true}),
      member_id: new FormControl(0, {nonNullable: true}),
      email: new FormControl('', {nonNullable: true}),
      email_type: new FormControl<EmailType>(EmailType.Home, {nonNullable: true})
    });
  }

  static generateDialogInput(context: Email, action: EditAction)
    : EditDialogInput<Email, EmailEditorComponent> {

    return {
      title: 'test',
      context: context,
      action: action,
      editorConfig: {
        component: EmailEditorComponent,
        form: this.generateForm()
      }
    }
  }

  static generateColumDefs(): ColumnDef<EmailMember>[] {
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
}
