import {Component} from '@angular/core';

import {AuthService} from '@app/core/auth/services/auth.service';

import {SortablePageableTableComponent} from '@app/shared/components/sortable-pageable-table/sortable-pageable-table.component';
import {BaseTableComponent} from '@app/shared/components/base-table-component/base-table.component';
import {EditAction, EditEvent} from '@app/shared/components/edit-dialog/models';

import {EmailCreate, EmailMember, Email} from '@app/features/emails/models/email';
import {EmailService} from '@app/features/emails/services/email.service';
import {EmailTableService} from '@app/features/emails/services/email-table.service';
import {EmailEditDialogService} from '@app/features/emails/services/email-edit-dialog.service';

@Component({
  selector: 'app-list-emails',
  imports: [SortablePageableTableComponent],
  providers: [EmailService, AuthService],
  templateUrl: './list-emails.component.html',
  styleUrl: './list-emails.component.scss'
})
export class ListEmailsComponent
  extends BaseTableComponent<EmailCreate, Email, EmailMember> {

  constructor(auth: AuthService,
              svc: EmailService,
              tableSvc: EmailTableService,
              dialogSvc: EmailEditDialogService) {
      super(auth, svc, tableSvc, dialogSvc);
  }

  onEditClick(event: EditEvent<EmailMember>): void {
    const title = 'emails.list.dialog.update'
    const context = event.data as Email;

    this.openEditDialog(this.dialogSvc.generateDialogInput(title, context, EditAction.Edit)).subscribe({
      next: result => console.log(result),
      error: err => this.errors.set(err)
    });
  }

  onDeleteClick(event: EditEvent<EmailMember>): void {
    const title = 'emails.list.dialog.delete';
    const context = event.data as Email;

    this.openEditDialog(this.dialogSvc.generateDialogInput(title, context, EditAction.Delete)).subscribe({
      next: result => console.log(result),
      error: err => this.errors.set(err)
    });
  }

}
