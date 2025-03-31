import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {first, merge, mergeMap, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";

import {AuthService} from '@app/core/auth';

import {SortablePageableTableComponent} from "@app/shared/components/sortable-pageable-table";
import {BaseTableComponent} from "@app/shared/components/base-table-component/base-table-component";
import {EditAction, EditDialogResult, EditEvent} from '@app/shared/components/edit-dialog';
import {PagedData} from '@app/shared/services/api-service';

import {
  PhoneEditDialogService, PhoneService, PhoneTableService, Phone, PhoneMember
} from '@app/features/phones';

@Component({
  selector: 'app-list-phones',
  imports: [SortablePageableTableComponent],
  providers: [
    PhoneService,
    AuthService,
    PhoneTableService,
    PhoneEditDialogService
  ],
  templateUrl: './list-phones.component.html',
  styleUrl: './list-phones.component.scss'
})
export class ListPhonesComponent extends BaseTableComponent<PhoneMember> implements OnInit, AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<PhoneMember>;

  constructor(auth: AuthService,
              private svc: PhoneService,
              private tableSvc: PhoneTableService,
              private dialogSvc: PhoneEditDialogService) {
    super(auth);
  }

  ngOnInit() {
    this.columns.set(this.tableSvc.getColumnDefs());
  }

  ngAfterViewInit(): void {
    merge(this._table.sort.sortChange, this._table.paginator.page)
        .pipe(
            startWith({}),
            switchMap(() => this.getCurrentPage())
        ).subscribe({
          // set the data source with the new page
          next: data => this.processPageData(data),
          error: (err: HttpErrorResponse) => this.processRequestError(err)
        });
  }

  onEditClick(event: EditEvent<PhoneMember>) {
    this.openDialog('phones.list.dialog.update', event, EditAction.Edit);
  }

  onDeleteClick(event: EditEvent<PhoneMember>) {
    this.openDialog('phones.list.dialog.delete', event, EditAction.Delete);
  }

  private getCurrentPage(): Observable<PagedData<PhoneMember>> {
    const pr = this.getPageRequest(
      this._table.paginator.pageIndex, this._table.paginator.pageSize,
      this._table.sort.active, this._table.sort.direction);

    return this.svc.getPagedData(pr).pipe(first());
  }

  private openDialog(title: string, event: EditEvent<PhoneMember>, action: EditAction): void {
    const dialogInput = this.dialogSvc.generateDialogInput(title, event.data as Phone, action);

    this.dialogSvc.openDialog(dialogInput).subscribe({
      next: (result: EditDialogResult<Phone>) => {
        if(result.action == EditAction.Edit) {
          this.updatePhone(result.context);
        } else if(result.action == EditAction.Delete) {
          this.deletePhone(result.context.id);
        }
      }
    })
  }

  private updatePhone(phone: Phone): void {
    this.svc.update(phone.id, phone).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    })
  }

  private deletePhone(id: number): void{
    this.svc.delete(id).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    })
  }
}
