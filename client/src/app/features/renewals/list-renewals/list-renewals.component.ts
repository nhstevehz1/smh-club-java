import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {first, merge, mergeMap, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";

import {AuthService} from '@app/core/auth';
import {SortablePageableTableComponent} from "@app/shared/components/sortable-pageable-table";
import {BaseTableComponent} from "@app/shared/components/base-table-component/base-table-component";

import {EditAction, EditDialogResult, EditEvent} from '@app/shared/components/edit-dialog';

import {
  Renewal,
  RenewalEditDialogService,
  RenewalMember,
  RenewalService,
  RenewalTableService
} from '@app/features/renewals';
import {PagedData} from '@app/shared/services';

@Component({
  selector: 'app-list-renewals',
  imports: [SortablePageableTableComponent],
  providers: [RenewalService, AuthService],
  templateUrl: './list-renewals.component.html',
  styleUrl: './list-renewals.component.scss'
})
export class ListRenewalsComponent extends BaseTableComponent<RenewalMember> implements OnInit, AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<RenewalMember>;

  constructor(auth: AuthService,
              private svc: RenewalService,
              private tableSvc: RenewalTableService,
              private dialogSvc: RenewalEditDialogService) {
    super(auth);
  }

  ngOnInit() {
    this.columns.set(this.tableSvc.getColumnDefs());
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
    this.openDialog('renewals.list.dialog.update',event, EditAction.Edit);
  }

  onDeleteClick(event: EditEvent<RenewalMember>): void {
    this.openDialog('renewals.list.dialog.delete',event, EditAction.Delete);
  }

  private getCurrentPage(): Observable<PagedData<RenewalMember>> {
    // assemble the dynamic page request
    const pr = this.getPageRequest(
      this._table.paginator.pageIndex, this._table.paginator.pageSize,
      this._table.sort.active, this._table.sort.direction);
    return this.svc.getPagedData(pr).pipe(first());
  }

  private openDialog(title: string, event: EditEvent<RenewalMember>, action: EditAction): void {
    const dialogInput = this.dialogSvc.generateDialogInput(title, event.data as Renewal, action);

    this.dialogSvc.openDialog(dialogInput).subscribe({
      next: (result: EditDialogResult<Renewal>) => {
        if(result.action == EditAction.Edit) {
          this.updateRenewal(result.context);
        } else if (result.action == EditAction.Delete) {
          this.deleteRenewal(event.data.id);
        }
      }
    });
  }

  private updateRenewal(renewal: Renewal): void {
    this.svc.update(renewal.id, renewal).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    })
  }

  private deleteRenewal(id: number) {
    this.svc.delete(id).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    })
  }
}
