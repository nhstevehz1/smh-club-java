import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
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

  resultsLength = 0;
  datasource = new MatTableDataSource<PhoneMember>();
  columns: ColumnDef<PhoneMember>[] = [];

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
              let pr = this.getPageRequest(this._table.paginator, this._table.sort);

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
        ).subscribe(data => this.datasource.data = data!); // set the data source with the new page
  }

  protected getColumns(): ColumnDef<PhoneMember>[] {
    return [
      {
        name: 'phone_number',
        displayName: 'Phone',
        isSortable: true,
        cell: (element: PhoneMember) => this.getPhoneNumber(element)
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

  private getPhoneNumber(phoneMember: PhoneMember): string {
    const code = phoneMember.country_code;
    const number = phoneMember.phone_number;

    // format phone number exp: (555) 555-5555
    const regex = /^(\d{3})(\d{3})(\d{4})$/;
    const formated = number.replace(regex, '($1) $2-$3');

    return `+${code} ${formated}`;
  }
}
