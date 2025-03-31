import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {first, merge, mergeMap, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";

import {AuthService} from '@app/core/auth';

import {
  SortablePageableTableComponent
} from "@app/shared/components/sortable-pageable-table";
import {BaseTableComponent} from "@app/shared/components/base-table-component/base-table-component";
import {EditAction, EditDialogResult, EditEvent} from '@app/shared/components/edit-dialog';
import {PagedData} from '@app/shared/services/api-service';

import {
  EmailEditDialogService, EmailService, EmailTableService,
  Email, EmailMember
} from '@app/features/emails';

@Component({
  selector: 'app-list-emails',
  imports: [SortablePageableTableComponent],
  providers: [EmailService, AuthService],
  templateUrl: './list-emails.component.html',
  styleUrl: './list-emails.component.scss'
})
export class ListEmailsComponent
  extends BaseTableComponent<EmailMember> implements OnInit, AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<EmailMember>;

  constructor(auth: AuthService,
              private svc: EmailService,
              private tableSvc: EmailTableService,
              private dialogSvc: EmailEditDialogService) {
      super(auth);
  }

  ngOnInit() {
    this.columns.set(this.tableSvc.getColumnDefs());
  }

  ngAfterViewInit(): void {
    merge(this._table.sort.sortChange, this._table.paginator.page).pipe(
        startWith({}),
        switchMap(() => this.getCurrentPage())
      ).subscribe({
        // set the data source with the new page
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse) => this.processRequestError(err)
      });
  }

  onEditClick(event: EditEvent<EmailMember>): void {
    this.openDialog('emails.list.dialog.update', event, EditAction.Edit);
  }

  onDeleteClick(event: EditEvent<EmailMember>): void {
    this.openDialog('emails.list.dialog.delete', event, EditAction.Delete);
  }

  private getCurrentPage(): Observable<PagedData<EmailMember>> {
    const pr = this.getPageRequest(
      this._table.paginator.pageIndex, this._table.paginator.pageSize,
      this._table.sort.active, this._table.sort.direction);
    return this.svc.getPagedData(pr).pipe(first());
  }

  private openDialog(title: string, event: EditEvent<EmailMember>, action: EditAction): void {
    const dialogInput = this.dialogSvc.generateDialogInput(title, event.data as Email, action);

    this.dialogSvc.openDialog(dialogInput).subscribe({
      next: (result: EditDialogResult<Email>) =>  {
        if(result.action == EditAction.Edit) {
          this.updateEmail(result.context);
        } else if(result.action == EditAction.Delete) {
          this.deleteEmail(event.data.id);
        }
      }
    });
  }

  private updateEmail(email: Email): void {
    this.svc.update(email.id, email).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    })
  }

  private deleteEmail(id: number): void {
    this.svc.delete(id).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    });
  }
}
