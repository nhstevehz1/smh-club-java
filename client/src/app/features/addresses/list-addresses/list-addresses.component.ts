import {AfterViewInit, Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {first, merge, mergeMap, Observable} from 'rxjs';
import {startWith, switchMap} from 'rxjs/operators';

import {AuthService} from '@app/core/auth';

import {SortablePageableTableComponent} from '@app/shared/components/sortable-pageable-table';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table';
import {BaseTableComponent} from '@app/shared/components/base-table-component/base-table-component';
import {PagedData} from '@app/shared/services/api-service';
import {EditAction, EditDialogResult, EditEvent} from '@app/shared/components/edit-dialog';

import {
  AddressEditDialogService, AddressService, AddressTableService,
  Address, AddressMember
} from '@app/features/addresses';



@Component({
  selector: 'app-list-addresses',
  imports: [SortablePageableTableComponent],
  providers: [AddressService, AddressTableService, AddressEditDialogService],
  templateUrl: './list-addresses.component.html',
  styleUrl: './list-addresses.component.scss',
  encapsulation: ViewEncapsulation.ShadowDom
})
export class ListAddressesComponent extends BaseTableComponent<AddressMember> implements OnInit, AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<ColumnDef<AddressMember>>;

  constructor(auth: AuthService,
              private svc: AddressService,
              private dialogSvc: AddressEditDialogService,
              private tableSvc: AddressTableService) {
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

  onEditClick(event: EditEvent<AddressMember>): void {
    this.openDialog('addresses.list.dialog.update', event, EditAction.Edit);
  }

  onDeleteClick(event: EditEvent<AddressMember>): void {
    this.openDialog('addresses.list.dialog.update', event, EditAction.Delete);
  }

  private getCurrentPage(): Observable<PagedData<AddressMember>> {
    // assemble the dynamic page request
    const pr = this.getPageRequest(
      this._table.paginator.pageIndex, this._table.paginator.pageSize,
      this._table.sort.active, this._table.sort.direction);
    return this.svc.getPagedData(pr).pipe(first());
  }

  private openDialog(title: string, event: EditEvent<AddressMember>, action: EditAction): void {
   const dialogInput = this.dialogSvc.generateDialogInput(title, event.data as Address, action);

    this.dialogSvc.openDialog(dialogInput).subscribe({
      next: (result: EditDialogResult<Address>) =>  {
        if(result.action == EditAction.Edit) {
          this.updateAddress(result.context);
        } else if(result.action == EditAction.Delete) {
          this.deleteAddress(event.data.id);
        }
      }
    });
  }

  private updateAddress(address: Address): void {
    this.svc.update(address.id, address).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    })
  }

  private deleteAddress(id: number) {
    this.svc.delete(id).pipe(
      mergeMap(() => this.getCurrentPage())
    ).subscribe({
      next: data => this.processPageData(data),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    });
  }
}
