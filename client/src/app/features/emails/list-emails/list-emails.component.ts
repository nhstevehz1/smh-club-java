import {AfterViewInit, Component, computed, OnInit, signal, ViewChild} from '@angular/core';
import {Email, EmailMember} from "../models/email";
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {EmailService} from "../services/email.service";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {first, flatMap, merge, mergeMap, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";
import {MatDialog} from '@angular/material/dialog';
import {EmailEditorComponent} from '../email-editor/email-editor.component';
import {AuthService} from '../../../core/auth/services/auth.service';
import {EditDialogComponent} from '../../../shared/components/edit-dialog/edit-dialog.component';
import {HttpErrorResponse} from '@angular/common/http';
import {PagedData} from '../../../shared/models/paged-data';
import {EditAction, EditDialogInput, EditDialogResult, EditEvent} from '../../../shared/components/edit-dialog/models';
import {EmailEditDialogService} from '../services/email-edit-dialog.service';

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

  constructor(private svc: EmailService, auth: AuthService,
              private dialogSvc: EmailEditDialogService) {
      super(auth);
  }

  ngOnInit() {
    this.columns.set(this.svc.getColumnDefs());
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
    const dialogInput = this.svc.generateEmailDialogInput(title, event, action);

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
    this.svc.updateEmail(email).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    })
  }

  private deleteEmail(id: number): void {
    this.svc.deleteEmail(id).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    });
  }
}
