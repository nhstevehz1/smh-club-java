import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {RenewalService} from "../services/renewal.service";
import {MatTableDataSource} from "@angular/material/table";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {RenewalMember} from "../models/renewal";

@Component({
  selector: 'app-list-renewals',
  imports: [SortablePageableTableComponent],
  providers: [RenewalService],
  templateUrl: './list-renewals.component.html',
  styleUrl: './list-renewals.component.scss'
})
export class ListRenewalsComponent
    extends BaseTableComponent<RenewalMember> implements OnInit, AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<RenewalMember>;

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
              const pr = this.getPageRequest(
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
        ).subscribe({
          // set the data source with the new page
          next: data => this.datasource.data = data!
        });
  }

  protected getColumns(): ColumnDef<RenewalMember>[] {
    return [
      {
        columnName: 'renewal_date',
        displayName: 'renewals.list.columns.renewalDate',
        isSortable: true,
        cell: (element: RenewalMember) => `${element.renewal_date}`,
      },
      {
        columnName: 'renewal_year',
        displayName: 'renewals.list.columns.renewalYear',
        isSortable: true,
        cell: (element: RenewalMember) => `${element.renewal_year}`
      },
      {
        columnName: 'full_name',
        displayName: 'renewals.list.columns.fullName',
        isSortable: true,
        cell: (element: RenewalMember) => this.getFullName(element.full_name)
      }
    ];
  }
}
