import {AfterViewInit, Component, computed, OnInit, signal, ViewChild, WritableSignal} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {PhoneService} from "../services/phone.service";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {Phone, PhoneMember} from "../models/phone";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {MatTableDataSource} from "@angular/material/table";
import {first, merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {PhoneType} from "../models/phone-type";
import {HttpErrorResponse} from '@angular/common/http';
import {PermissionType} from '../../../core/auth/models/permission-type';
import {AuthService} from '../../../core/auth/services/auth.service';
import {EditEvent} from '../../../shared/components/edit-dialog/models/edit-event';

@Component({
  selector: 'app-list-phones',
  imports: [SortablePageableTableComponent],
  providers: [
    PhoneService,
    AuthService
  ],
  templateUrl: './list-phones.component.html',
  styleUrl: './list-phones.component.scss'
})
export class ListPhonesComponent extends BaseTableComponent<PhoneMember> implements AfterViewInit {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<PhoneMember>;

  resultsLength = signal(0);
  datasource = signal(new MatTableDataSource<PhoneMember>());
  columns: WritableSignal<ColumnDef<PhoneMember>[]>;

  hasWriteRole = computed(() => this.auth.hasPermission(PermissionType.write));

  private phoneTypeMap = new Map<PhoneType, string>([
     [PhoneType.Work, 'phones.type.work'],
     [PhoneType.Home, 'phones.type.home'],
     [PhoneType.Mobile, 'phones.type.mobile']
  ]);

  constructor(private svc: PhoneService,
              private auth: AuthService) {
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
              return this.svc.getPhones(pr).pipe(first());
            })
        ).subscribe({
          // set the data source with the new page
          next: data => {
            this.datasource().data = data._content;
            this.resultsLength.update(() => data.page.totalElements);
          },
          error: (err: HttpErrorResponse) => {
            console.debug(err);
            this.datasource().data = [];
          }
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

  onEditClick(event: EditEvent<Phone>) {

  }
}
