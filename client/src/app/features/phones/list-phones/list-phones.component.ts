import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {PhoneService} from "../services/phone.service";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {PhoneMember} from "../models/phone";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {MatTableDataSource} from "@angular/material/table";
import {merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {PhoneType} from "../models/phone-type";

@Component({
  selector: 'app-list-phones',
  imports: [SortablePageableTableComponent],
  providers: [PhoneService],
  templateUrl: './list-phones.component.html',
  styleUrl: './list-phones.component.scss'
})
export class ListPhonesComponent extends BaseTableComponent<PhoneMember> implements OnInit, AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<PhoneMember>;

  resultsLength = 0;
  datasource = new MatTableDataSource<PhoneMember>();
  columns: ColumnDef<PhoneMember>[] = [];

  private phoneTypeMap = new Map<PhoneType, string>([
     [PhoneType.Work, 'phones.type.work'],
     [PhoneType.Home, 'phones.type.home'],
     [PhoneType.Mobile, 'phones.type.mobile']
  ]);

  constructor(private svc: PhoneService) {
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
              return this.svc.getPhones(pr)
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

  protected getColumns(): ColumnDef<PhoneMember>[] {
    return [
      {
        columnName: 'phone_number',
        displayName: 'phones.list.columns.phoneNumber',
        isSortable: true,
        cell: (element: PhoneMember) => this.getPhoneNumber(element)
      },
      {
        columnName: 'phone_type',
        displayName: 'phones.list.columns.phoneType',
        isSortable: false,
        cell: (element: PhoneMember) => this.phoneTypeMap.get(element.phone_type)
      },
      {
        columnName: 'full_name',
        displayName: 'phones.list.columns.fullName',
        isSortable: true,
        cell: (element: PhoneMember) => this.getFullName(element.full_name)
      },
    ];
  }

  private getPhoneNumber(phoneMember: PhoneMember): string {
    const code = phoneMember.country_code;
    const number = phoneMember.phone_number;

    // format phone number exp: (555) 555-5555
    const regex = /^(\d{3})(\d{3})(\d{4})$/;
    const formated = number.replace(regex, '($1) $2-$3');

    return `+${code} ${formated}`;
  }
}
