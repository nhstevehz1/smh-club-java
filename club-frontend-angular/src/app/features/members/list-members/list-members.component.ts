import {AfterViewInit, Component, inject, ViewChild} from '@angular/core';
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef,
  MatRow, MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {MembersService} from '../services/members.service';
import {Member} from '../models/Member';
import {MatSort, MatSortHeader} from '@angular/material/sort';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {PageRequest, Sort} from '../../../shared/models/page-request';
import {merge, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import {ConcatStringsPipe} from "../../../shared/pipes/strings/concat-strings.pipe";

@Component({
  selector: 'app-list-members',
  imports: [
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatSort,
    MatSortHeader,
    MatHeaderRow,
    MatRow,
    MatPaginator,
    MatCell,
    ConcatStringsPipe,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRowDef,
    MatRowDef,

  ],
  providers: [MembersService],
  templateUrl: './list-members.component.html',
  styleUrl: './list-members.component.scss'
})
export class ListMembersComponent implements AfterViewInit {
  private _svc: MembersService = inject(MembersService);
  private _dataSource: MatTableDataSource<Member> = new MatTableDataSource();
  private _columnNames: string[] = ['member_number', 'first_name', 'last_name', 'birth_date', 'joined_date'];
  private _pageSizes: number[] = [5,10,25,100];
  private _resultsLength: number = 0;
  private _isLoadingData: boolean = false;

  get dataSource(): MatTableDataSource<Member> {
    return this._dataSource;
  }
  get columnNames(): string[] {
    return this._columnNames;
  }
  get pageSizes(): number[] {
    return this._pageSizes;
  }
  get resultsLength(): number {
    return this._resultsLength;
  }
  get isLoadingData(): boolean {
    return this._isLoadingData;
  }

  @ViewChild(MatSort, {static: true}) sort: MatSort | null = null;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator | null = null;

  ngAfterViewInit() {
    // revert back to page 0 if it had been changed.
    this.sort!.sortChange.subscribe(() => this.paginator!.pageIndex = 0);

    merge(this.sort!.sortChange, this.paginator!.page)
    .pipe(
        startWith({}),
        switchMap(() => {
          // set loading data flag
          this._isLoadingData = true;

          // assemble the dynamic page request
          let pr = this.getPageRequest();

          // pipe any errors to an Observable of null
          return this._svc.getMembers(pr)
              .pipe(catchError(err => {
                console.log(err);
                return observableOf(null);
              }));
        }),
        map(data => {
          // reset loading data flag
          this._isLoadingData = false;

          // if the data returned s null due to an error.  Map the null data to an empty array
          if (data === null) {
            return [];
          }

          // set the results length in case it has changed.
          this._resultsLength = data.page.totalElements;

          // map the content array only
          return data._content;
        })
    ).subscribe(data => this._dataSource.data = data!); // set the data source with the new page
  }

  onPageEvent(e: PageEvent) {
    console.log(e);
  }

  private getPageRequest(): PageRequest {
    let page = this.paginator?.pageIndex;
    let size = this.paginator?.pageSize;

    let pr = PageRequest.of(
        page,
        size,
    )

    // The dynamic page request supports multiple sort fields.
    // currently, our implementation supports only single column sort
    if(this.sort!.active !== undefined) {
      let sort = Sort.of(this.sort!.active, this.sort!.direction);
      pr.addSort(sort);
    }
    return  pr;
  }
}
