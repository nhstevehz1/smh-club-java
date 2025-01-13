import {AfterViewInit, Component, inject, OnInit, ViewChild} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {PhoneService} from "../services/phone.service";
import {TableComponentBase} from "../../../shared/components/table-component-base/table-component-base";
import {PhoneMember} from "../models/phone-member";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {MatTableDataSource} from "@angular/material/table";
import {merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-list-phones',
  imports: [SortablePageableTableComponent],
  providers: [PhoneService],
  templateUrl: './list-phones.component.html',
  styleUrl: './list-phones.component.scss'
})
export class ListPhonesComponent extends TableComponentBase<PhoneMember> implements OnInit, AfterViewInit {
  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent;

  private _svc = inject(PhoneService);

  resultsLength = 0;
  datasource = new MatTableDataSource<PhoneMember>();
  columns: ColumnDef<PhoneMember>[] = [];

  ngOnInit(): void {
    this.columns = this.getColumns();
  }

  ngAfterViewInit(): void {
    merge(this._table.sort.sortChange, this._table.paginator.page)
        .pipe(
            startWith({}),
            switchMap(() => {
              // assemble the dynamic page request
              let pr = this.getPageRequest(this._table.paginator, this._table.sort);

              // pipe any errors to an Observable of null
              return this._svc.getPhones(pr)
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

  protected getColumns(): ColumnDef<PhoneMember>[] {
    return [
      {
        name: 'phone_number',
        displayName: 'Phone',
        isSortable: true,
        cell: (element: PhoneMember) => `${element.phone_number}`
      },
      {
        name: 'phone_type',
        displayName: 'Type',
        isSortable: true,
        cell: (element: PhoneMember) => `${element.phone_type}`
      },
      {
        name: 'member_number',
        displayName: 'No.',
        isSortable: true,
        cell: (element: PhoneMember) => `${element.member_number}`
      },
      {
        name: 'full_name',
        displayName: 'Member',
        isSortable: true,
        cell: (element: PhoneMember) => this.getFullName(element.full_name)
      },
    ];
  }

}
