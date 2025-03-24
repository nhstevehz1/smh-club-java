import {AfterViewInit, Component, computed, signal, ViewChild, ViewEncapsulation, WritableSignal} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {AddressService} from "../services/address.service";
import {MatTableDataSource} from "@angular/material/table";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {Address, AddressMember} from "../models/address";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {AddressType} from "../models/address-type";
import {AuthService} from '../../../core/auth/services/auth.service';
import {PermissionType} from '../../../core/auth/models/permission-type';
import {MatDialog} from '@angular/material/dialog';
import {EditAddressDialogComponent} from '../update-address-dialog/edit-address-dialog.component';
import {EditAction, EditDialogData, EditEvent} from '../../../shared/models/edit-event';

@Component({
  selector: 'app-list-addresses',
  imports: [SortablePageableTableComponent],
  providers: [AddressService],
  templateUrl: './list-addresses.component.html',
  styleUrl: './list-addresses.component.scss',
  encapsulation: ViewEncapsulation.ShadowDom
})
export class ListAddressesComponent extends BaseTableComponent<AddressMember> implements AfterViewInit {
  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<ColumnDef<AddressMember>>;

  resultsLength = signal(0);
  datasource = signal(new MatTableDataSource<AddressMember>());
  columns: WritableSignal<ColumnDef<AddressMember>[]>;

  readonly canEdit = computed(() => this.auth.hasPermission(PermissionType.write));

  private readonly addressTypeMap: Map<AddressType, string> = new Map<AddressType, string>([
      [AddressType.Home, 'addresses.type.home'],
      [AddressType.Work, 'addresses.type.work'],
      [AddressType.Other, 'addresses.type.other']
  ]);

  constructor(private svc: AddressService,
              private auth: AuthService,
              private dialog: MatDialog) {
    super();
    this.columns = signal(this.getColumns());
  }

  ngAfterViewInit(): void {
    merge(this._table.sort.sortChange, this._table.paginator.page)
        .pipe(
            startWith({}),
            switchMap(() => {
              // assemble the dynamic page request
              const pr = this.getPageRequest(
                  this._table.paginator.pageIndex, this._table.paginator.pageSize,
                  this._table.sort.active, this._table.sort.direction);

              // pipe any errors to an Observable of null
              return this.svc.getAddresses(pr)
                  .pipe(catchError(err => {
                    console.log(err);
                    return observableOf(null);
                  }));
            }),
            map(data => {
              // if the data returned s null due to an error.  Map the null data to an empty array
              if (data === null) {
                return [];
              }

              // set the results length in case it has changed.
              this.resultsLength.set(data.page.totalElements);

              // map the content array only
              return data._content;
            })
        ).subscribe({
          // set the data source with the new page
          next: data => this.datasource().data = data!
        });
  }

  protected getColumns(): ColumnDef<AddressMember>[] {
    return [
      {
        columnName: 'address1',
        displayName: 'addresses.list.columns.address',
        isSortable: true,
        cell: (element: AddressMember) => this.getStreet(element)
      },
      {
        columnName: 'city',
        displayName: 'addresses.list.columns.city',
        isSortable: true,
        cell: (element: AddressMember) => `${element.city}`
      },
      {
        columnName: 'state',
        displayName: 'addresses.list.columns.state',
        isSortable: true,
        cell: (element: AddressMember) => `${element.state}`
      },
      {
        columnName: 'zip',
        displayName: 'addresses.list.columns.postalCode',
        isSortable: true,
        cell: (element: AddressMember) => `${element.postal_code}`
      },
      {
        columnName: 'address_type',
        displayName: 'addresses.list.columns.addressType',
        isSortable: false,
        cell: (element: AddressMember) => this.addressTypeMap.get(element.address_type)//`${element.address_type}`
      },
      {
        columnName: 'full_name',
        displayName: 'addresses.list.columns.fullName',
        isSortable: true,
        cell: (element: AddressMember) =>  this.getFullName(element.full_name) //`${element.full_name.last_first}`
      }
    ];
  }

  onEditClick(event: EditEvent<AddressMember>): void {
    this.openDialog(event, EditAction.Edit);
  }

  private openDialog(event: EditEvent<AddressMember>, action: EditAction) {
    const dialogData: EditDialogData<Address> = {
      data: event.data as Address,
      action: action
    }

    const dialogRef
      = this.dialog.open<EditAddressDialogComponent, EditDialogData<Address>>(
        EditAddressDialogComponent, {data: dialogData});

    dialogRef.afterClosed().subscribe({
      next: (result: EditDialogData<Address>) => {
        if(result.action == EditAction.Edit) {
          const address = result.data as AddressMember;
          address.full_name = event.data.full_name;
          this.updateRowData(address, event.idx);
        } else if (result.action == EditAction.Delete) {
          this.deleteRowData(event.idx);
        }
      }
    });
  }

  private updateRowData(address: AddressMember, idx: number): void {
    this.datasource().data[idx] = address;
  }

  private deleteRowData(idx: number) {
    this.datasource().data.splice(idx, 1);
  }

  private getStreet(address: AddressMember): string {
    const street1 = address.address1;
    const street2 = address.address2 || ''
    return street2.length > 0 ? `${street1}, ${street2}` : street1;
  }

}
