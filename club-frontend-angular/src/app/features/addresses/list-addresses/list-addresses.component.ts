import {AfterViewInit, Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {AddressService} from "../services/address.service";
import {MatTableDataSource} from "@angular/material/table";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {AddressMember} from "../models/address-member";
import {TableComponentBase} from "../../../shared/components/table-component-base/table-component-base";
import {delay, merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-list-addresses',
  imports: [SortablePageableTableComponent],
  providers: [AddressService],
  templateUrl: './list-addresses.component.html',
  styleUrl: './list-addresses.component.scss',
  encapsulation: ViewEncapsulation.ShadowDom
})
export class ListAddressesComponent extends TableComponentBase<AddressMember> implements OnInit, AfterViewInit {
  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent;

  resultsLength = 0;
  datasource = new MatTableDataSource<AddressMember>();
  columns: ColumnDef<AddressMember>[] = [];

  constructor(private svc: AddressService) {
    super();
  }

  ngOnInit(): void {
    this.columns = this.getColumns();
  }

  ngAfterViewInit(): void {
    merge(this._table.sort.sortChange, this._table.paginator.page)
        .pipe(
            startWith({}),
            switchMap(() => {
              // assemble the dynamic page request
              let pr = this.getPageRequest(
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
              this.resultsLength = data.page.totalElements;

              // map the content array only
              return data._content;
            })
        ).subscribe(data => this.datasource.data = data!); // set the data source with the new page
  }

  protected getColumns(): ColumnDef<AddressMember>[] {
    return [

      {
        columnName: 'address1',
        displayName: 'Address',
        isSortable: true,
        cell: (element: AddressMember) => this.getStreet(element)
      },
      {
        columnName: 'city',
        displayName: 'City',
        isSortable: true,
        cell: (element: AddressMember) => `${element.city}`
      },
      {
        columnName: 'state',
        displayName: 'State',
        isSortable: true,
        cell: (element: AddressMember) => `${element.state}`
      },
      {
        columnName: 'zip',
        displayName: 'Zip',
        isSortable: true,
        cell: (element: AddressMember) => `${element.zip}`
      },
      {
        columnName: 'address_type',
        displayName: 'Type',
        isSortable: true,
        cell: (element: AddressMember) => `${element.address_type}`
      },
      {
        columnName: 'member_number',
        displayName: 'No.',
        isSortable: true,
        cell: (element: AddressMember) => `${element.member_number}`
      },
      {
        columnName: 'full_name',
        displayName: 'Member',
        isSortable: true,
        cell: (element: AddressMember) =>  this.getFullName(element.full_name) //`${element.full_name.last_first}`
      }
    ];
  }

  private getStreet(address: AddressMember): string {
    const street1 = address.address1;
    const street2 = address.address2 || ''
    return street2.length > 0 ? `${street1}, ${street2}` : street1;
  }
}
