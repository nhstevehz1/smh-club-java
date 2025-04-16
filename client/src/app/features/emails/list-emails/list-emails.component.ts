import {Component} from '@angular/core';

import {AuthService} from '@app/core/auth/services/auth.service';

import {
  SortablePageableTableComponent
} from '@app/shared/components/sortable-pageable-table/sortable-pageable-table.component';
import {BaseTableComponent} from '@app/shared/components/base-table-component/base-table.component';
import {EditAction, EditEvent} from '@app/shared/components/base-edit-dialog/models';

import {EmailMember, Email} from '@app/features/emails/models/email';
import {EmailService} from '@app/features/emails/services/email.service';
import {EmailTableService} from '@app/features/emails/services/email-table.service';
import {EmailEditDialogService} from '@app/features/emails/services/email-edit-dialog.service';
import {EmailEditorComponent} from '@app/features/emails/email-editor/email-editor.component';
import {mergeMap, of} from 'rxjs';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-list-emails',
  imports: [SortablePageableTableComponent],
  providers: [
    EmailService,
    AuthService,
    EmailTableService,
    EmailEditDialogService
  ],
  templateUrl: './list-emails.component.html',
  styleUrl: './list-emails.component.scss'
})
export class ListEmailsComponent
  extends BaseTableComponent<Email, EmailMember, EmailEditorComponent> {

  constructor(auth: AuthService,
              svc: EmailService,
              tableSvc: EmailTableService,
              dialogSvc: EmailEditDialogService) {
      super(auth, svc, tableSvc, dialogSvc);
  }

  onEditClick(event: EditEvent<EmailMember>): void {
    const title = 'emails.list.dialog.update'
    const context = event.data as Email;
    const dialogInput = this.dialogSvc.generateDialogInput(title, context, EditAction.Edit);

    this.openEditDialog(dialogInput).pipe(
      mergeMap(emailResult => {
        if(emailResult.action == EditAction.Edit) {
          return this.apiSvc.update(emailResult.context);
        } else {
          return of(null);
        }
      })
    ).subscribe({
      next: emailResult => {
        if(emailResult) {
          const update: EmailMember = {
            id: emailResult.id,
            member_id: emailResult.member_id,
            email: emailResult.email,
            email_type: emailResult.email_type,
            full_name: event.data.full_name
          }
          this.updateItem(update);
        }
      },
      error: err => this.errors.set(err)
    });
  }

  onDeleteClick(event: EditEvent<EmailMember>): void {
    const title = 'emails.list.dialog.delete';
    const context = event.data as Email;
    const dialogInput = this.dialogSvc.generateDialogInput(title, context, EditAction.Delete);

    this.openEditDialog(dialogInput).pipe(
      mergeMap(deleteResult => {
        if(deleteResult.action == EditAction.Delete) {
          return this.apiSvc.delete(deleteResult.context.id).pipe(
            map(() => deleteResult)
          );
        } else {
          return of(deleteResult)
        }
      })
    ).subscribe({
      next: email => {
        if(email.action == EditAction.Delete) {
          this.deleteItem(event.idx);
        }
      },
      error: err => this.errors.set(err)
    });
  }

}
