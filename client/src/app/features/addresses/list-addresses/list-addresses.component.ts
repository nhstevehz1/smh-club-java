import {AfterViewInit, Component, ViewChild, ViewEncapsulation} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {AddressService} from "../services/address.service";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {Address, AddressMember} from "../models/address";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {first, merge, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";
import {AuthService} from '../../../core/auth/services/auth.service';
import {MatDialog} from '@angular/material/dialog';
import {EditAction, EditDialogData, EditEvent} from '../../../shared/components/edit-dialog/models/edit-event';
import {AddressEditorComponent} from '../address-editor/address-editor.component';
import {HttpErrorResponse} from '@angular/common/http';
import {EditDialogComponent} from '../../../shared/components/edit-dialog/edit-dialog.component';
import {PagedData} from '../../../shared/models/paged-data';

@Component({
  selector: 'app-list-addresses',
  imports: [SortablePageableTableComponent],
  providers: [AddressService, AddressService],
  templateUrl: './list-addresses.component.html',
  styleUrl: './list-addresses.component.scss',
  encapsulation: ViewEncapsulation.ShadowDom
})
export class ListAddressesComponent extends BaseTableComponent<AddressMember> implements AfterViewInit {
  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<ColumnDef<AddressMember>>;

  constructor(private svc: AddressService,
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

  onEditClick(event: EditEvent<AddressMember>): void {
    this.openDialog(event, EditAction.Edit);
  }

  onDeleteClick(event: EditEvent<AddressMember>): void {
    this.openDialog(event, EditAction.Delete);
  }

  private getCurrentPage(): Observable<PagedData<AddressMember>> {
    // assemble the dynamic page request
    const pr = this.getPageRequest(
      this._table.paginator.pageIndex, this._table.paginator.pageSize,
      this._table.sort.active, this._table.sort.direction);
    return this.svc.getAddresses(pr).pipe(first());
  }

  private openDialog(event: EditEvent<AddressMember>, action: EditAction): void {
   const dialogData: EditDialogData<Address> = {
     title: 'Address Title',
     component: AddressEditorComponent,
     form: this.svc.generateAddressForm(),
     context: event.data as Address,
     action: action
    }

    const dialogRef =
      this.dialog.open<EditDialogComponent<Address>, EditDialogData<Address>>(
        EditDialogComponent<Address>, {data: dialogData});

    dialogRef.afterClosed().subscribe({
      next: (result: EditDialogData<Address>) => {
        if(result.action == EditAction.Edit) {
          this.updateAddress(result.context);
        } else if (result.action == EditAction.Delete) {
          this.deleteEmail(event.data.id);
        }
      }
    });
  }

  private updateAddress(address: Address): void {
    this.svc.updateAddress(address).subscribe({
      next: () => this.getCurrentPage().subscribe({
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse) => this.processRequestError(err)
      }),
      error: (err: HttpErrorResponse)=> this.processRequestError(err)
    });
  }

  private deleteEmail(id: number) {
    this.svc.deleteAddress(id).subscribe({
      next: () => this.getCurrentPage().subscribe({
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse)=> this.processRequestError(err)
      }),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    });
  }
}
