import {AfterViewInit, Component, inject, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {AddressService} from "../services/address.service";
import {MatTableDataSource} from "@angular/material/table";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {AddressMember} from "../models/address-member";
import {TableComponentBase} from "../../../shared/components/table-component-base/table-component-base";
import {merge, of as observableOf, take} from "rxjs";
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
  private table!: SortablePageableTableComponent;

  private svc = inject(AddressService);

  resultsLength = 0;
  datasource = new MatTableDataSource<AddressMember>();
  columns: ColumnDef<AddressMember>[] = [];

  ngOnInit(): void {
    this.columns = this.getColumns();
  }

  ngAfterViewInit(): void {
    merge(this.table.sort.sortChange, this.table.paginator.page)
        .pipe(
            startWith({}),
            switchMap(() => {
              // assemble the dynamic page request
              let pr = this.getPageRequest(this.table.paginator, this.table.sort);

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
        name: 'member_number',
        displayName: 'No.',
        isSortable: true,
        cell: (element: AddressMember) => `${element.member_number}`
      },
      {
        name: 'full_name',
        displayName: 'Member',
        isSortable: true,
        cell: (element: AddressMember) => `${element.full_name.last_first}`
      },
      {
        name: 'address1',
        displayName: 'Address',
        isSortable: true,
        cell: (element: AddressMember) => (`${element.address1} ${element.address2}`).trim()
      },
      {
        name: 'city',
        displayName: 'City',
        isSortable: true,
        cell: (element: AddressMember) => `${element.city}`
      },
      {
        name: 'state',
        displayName: 'State',
        isSortable: true,
        cell: (element: AddressMember) => `${element.state}`
      },
      {
        name: 'zip',
        displayName: 'Zip',
        isSortable: true,
        cell: (element: AddressMember) => `${element.zip}`
      }
    ];
  }

}
