import {AfterViewInit, Component, computed, ViewChild} from '@angular/core';
import {Email, EmailMember} from "../models/email";
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {EmailService} from "../services/email.service";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {first, merge, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";
import {MatDialog} from '@angular/material/dialog';
import {EditAction, EditDialogData, EditEvent} from '../../../shared/components/edit-dialog/models/edit-event';
import {EmailEditorComponent} from '../email-editor/email-editor.component';
import {AuthService} from '../../../core/auth/services/auth.service';
import {PermissionType} from '../../../core/auth/models/permission-type';
import {EditDialogComponent} from '../../../shared/components/edit-dialog/edit-dialog.component';
import {HttpErrorResponse} from '@angular/common/http';
import {PagedData} from '../../../shared/models/paged-data';


@Component({
  selector: 'app-list-emails',
  imports: [SortablePageableTableComponent],
  providers: [EmailService, AuthService],
  templateUrl: './list-emails.component.html',
  styleUrl: './list-emails.component.scss'
})
export class ListEmailsComponent extends BaseTableComponent<EmailMember> implements AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<EmailMember>;

  constructor(private svc: EmailService,
              auth: AuthService,
              private dialog: MatDialog) {
      super(auth);
      this.columns.update(() => this.svc.getColumnDefs());
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
    this.openDialog(event, EditAction.Edit);
  }

  onDeleteClick(event: EditEvent<EmailMember>): void {
    this.openDialog(event, EditAction.Delete);
  }

  private getCurrentPage(): Observable<PagedData<EmailMember>> {
    const pr = this.getPageRequest(
      this._table.paginator.pageIndex, this._table.paginator.pageSize,
      this._table.sort.active, this._table.sort.direction);
    return this.svc.getEmails(pr).pipe(first());
  }

  private openDialog(event: EditEvent<EmailMember>, action: EditAction): void {
    const dialogData: EditDialogData<Email> = {
      title: 'my title',
      component: EmailEditorComponent,
      form: this.svc.generateEmailForm(),
      context: event.data as Email,
      action: action
    };

    const dialogRef =
      this.dialog.open<EditDialogComponent<Email>, EditDialogData<Email> >(
        EditDialogComponent<Email>, {data: dialogData}
      );

    dialogRef.afterClosed().subscribe({
      next: (result: EditDialogData<Email>) =>  {
        if(result.action == EditAction.Edit) {
          this.updateEmail(result.context);
        } else if(result.action == EditAction.Delete) {
          this.deleteEmail(event.data.id);
        }
      }
    });
  }

  private updateEmail(email: Email): void {
    this.svc.updateEmail(email).subscribe({
      next: () => this.getCurrentPage().subscribe({
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse) => this.processRequestError(err)
      }),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    })
  }

  private deleteEmail(id: number): void {
    this.svc.deleteEmail(id).subscribe({
      next: () => this.getCurrentPage().subscribe({
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse)=> this.processRequestError(err)
      }),
      error: (err: HttpErrorResponse)=> this.processRequestError(err)
    })
  }
}
