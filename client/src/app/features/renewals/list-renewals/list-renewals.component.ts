import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {RenewalService} from "../services/renewal.service";
import {first, merge, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {Renewal, RenewalMember} from "../models/renewal";
import {AuthService} from '../../../core/auth/services/auth.service';
import {EditAction, EditDialogData, EditEvent} from '../../../shared/components/edit-dialog/models/edit-event';
import {PagedData} from '../../../shared/models/paged-data';
import {HttpErrorResponse} from '@angular/common/http';
import {RenewalEditorComponent} from '../renewal-editor/renewal-editor.component';
import {MatDialog} from '@angular/material/dialog';
import {EditDialogComponent} from '../../../shared/components/edit-dialog/edit-dialog.component';

@Component({
  selector: 'app-list-renewals',
  imports: [SortablePageableTableComponent],
  providers: [RenewalService, AuthService],
  templateUrl: './list-renewals.component.html',
  styleUrl: './list-renewals.component.scss'
})
export class ListRenewalsComponent extends BaseTableComponent<RenewalMember> implements AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<RenewalMember>;

  constructor(private svc: RenewalService,
              auth: AuthService,
              private dialog: MatDialog) {
    super(auth);
    this.columns.set(this.svc.getColumnDefs())
  }

  ngAfterViewInit(): void {
    merge(this._table.sort.sortChange, this._table.paginator.page).pipe(
            startWith({}),
            switchMap(() => this.getCurrentPage()),
        ).subscribe({
          // set the data source with the new page
          next: data => this.processPageData(data),
          error: (err: HttpErrorResponse) => this.processRequestError(err)
        });
  }

  onEditClick(event: EditEvent<RenewalMember>): void {
    this.openDialog(event, EditAction.Edit);
  }

  onDeleteClick(event: EditEvent<RenewalMember>): void {
    this.openDialog(event, EditAction.Delete);
  }

  private getCurrentPage(): Observable<PagedData<RenewalMember>> {
    // assemble the dynamic page request
    const pr = this.getPageRequest(
      this._table.paginator.pageIndex, this._table.paginator.pageSize,
      this._table.sort.active, this._table.sort.direction);
    return this.svc.getRenewals(pr).pipe(first());
  }

  private openDialog(event: EditEvent<RenewalMember>, action: EditAction): void {
    const dialogData: EditDialogData<Renewal> = {
      title: 'Renewal Title',
      component: RenewalEditorComponent,
      form: this.svc.generateRenewalForm(),
      context: event.data as Renewal,
      action: action
    }

    const dialogRef =
      this.dialog.open<EditDialogComponent<Renewal>, EditDialogData<Renewal>>(
        EditDialogComponent<Renewal>, {data: dialogData});

    dialogRef.afterClosed().subscribe({
      next: (result: EditDialogData<Renewal>) => {
        if(result.action == EditAction.Edit) {
          this.updateRenewal(result.context);
        } else if (result.action == EditAction.Delete) {
          this.deleteRenewal(event.data.id);
        }
      }
    });
  }

  private updateRenewal(renewal: Renewal): void {
    this.svc.updateRenewal(renewal).subscribe({
      next: () => this.getCurrentPage().subscribe({
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse) => this.processRequestError(err)
      }),
      error: (err: HttpErrorResponse)=> this.processRequestError(err)
    });
  }

  private deleteRenewal(id: number) {
    this.svc.deleteRenewal(id).subscribe({
      next: () => this.getCurrentPage().subscribe({
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse)=> this.processRequestError(err)
      }),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    });
  }
}
