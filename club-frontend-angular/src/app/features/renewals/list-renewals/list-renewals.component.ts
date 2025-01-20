import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {RenewalService} from "../services/renewal.service";
import {MatTableDataSource} from "@angular/material/table";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {TableComponentBase} from "../../../shared/components/table-component-base/table-component-base";
import {RenewalMember} from "../models/renewal-member";

@Component({
  selector: 'app-list-renewals',
  imports: [SortablePageableTableComponent],
  providers: [RenewalService],
  templateUrl: './list-renewals.component.html',
  styleUrl: './list-renewals.component.scss'
})
export class ListRenewalsComponent
    extends TableComponentBase<RenewalMember> implements OnInit, AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent;

  resultsLength = 0;
  datasource = new MatTableDataSource<RenewalMember>();
  columns: ColumnDef<RenewalMember>[] = [];

  constructor(private svc: RenewalService) {
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
              return this.svc.getRenewals(pr)
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

  protected getColumns(): ColumnDef<RenewalMember>[] {
    return [
      {
        columnName: 'renewal_date',
        displayName: 'Renewal',
        isSortable: true,
        cell: (element: RenewalMember) => `${element.renewal_date}`,
      },
      {
        columnName: 'renewal_year',
        displayName: 'For Year',
        isSortable: true,
        cell: (element: RenewalMember) => `${element.renewal_year}`
      },
      {
        columnName: 'member_number',
        displayName: 'No.',
        isSortable: true,
        cell: (element: RenewalMember) => `${element.member_number}`
      },
      {
        columnName: 'full_name',
        displayName: 'Member',
        isSortable: true,
        cell: (element: RenewalMember) => this.getFullName(element.full_name)
      }
    ];
  }
}
