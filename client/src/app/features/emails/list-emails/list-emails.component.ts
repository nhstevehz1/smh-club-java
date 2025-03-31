import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {first, merge, mergeMap, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";

import {AuthService} from '@app/core/auth';

import {SortablePageableTableComponent} from "@app/shared/components/sortable-pageable-table";
import {BaseTableComponent} from "@app/shared/components/base-table-component";
import {EditAction, EditDialogResult, EditEvent} from '@app/shared/components/edit-dialog';
import {PagedData} from '@app/shared/services/api-service';

import {
  EmailEditDialogService, EmailService, EmailTableService,
  Email, EmailMember, EmailCreate
} from '@app/features/emails';

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
