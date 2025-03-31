import { OnInit, AfterViewInit, Component, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {HttpErrorResponse} from '@angular/common/http';

import {first, merge, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";

import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatTooltip} from "@angular/material/tooltip";

import {TranslatePipe, TranslateService} from '@ngx-translate/core';

import {AuthService} from '@app/core/auth';

import {SortablePageableTableComponent} from '@app/shared/components/sortable-pageable-table';
import {BaseTableComponent} from '@app/shared/components/base-table-component/base-table-component';
import {DateTimeToFormatPipe} from '@app/shared/pipes';
import {EditEvent} from '@app/shared/components/edit-dialog';
import {PagedData} from '@app/shared/services';

import {Member, MemberService, MemberTableService} from '@app/features/members';


@Component({
  selector: 'app-list-members',
  imports: [
    SortablePageableTableComponent,
    MatIconModule,
    MatButtonModule,
    MatTooltip,
    TranslatePipe,
  ],
    providers: [
      MemberService,
      MemberTableService,
      TranslateService,
      DateTimeToFormatPipe
    ],
  templateUrl: './list-members.component.html',
  styleUrl: './list-members.component.scss'
})
export class ListMembersComponent extends BaseTableComponent<Member> implements OnInit, AfterViewInit{

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<Member>;

  constructor(auth: AuthService,
              private svc: MemberService,
              private tableSvc: MemberTableService,
              private router: Router) {

    super(auth);
  }

  ngOnInit() {
    this.columns.set(this.tableSvc.getColumnDefs());
  }

  ngAfterViewInit(): void {
    merge(this._table.sort.sortChange, this._table.paginator.page).pipe(
        startWith({}),
        switchMap(() => this.getCurrentPage())
      ).subscribe({
        // set the data source to the new page
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse) => this.processRequestError(err)
      });
  }

  addMemberHandler(): void {
    this.router.navigate(['p/members/add']).then();
  }

  onViewClick(event: EditEvent<Member>): void {
    this.router.navigate(['p/member/view', event.data.id]).then();
  }

  private getCurrentPage(): Observable<PagedData<Member>> {
    // assemble the dynamic page request
    const pr = this.getPageRequest(
      this._table.paginator.pageIndex, this._table.paginator.pageSize,
      this._table.sort.active, this._table.sort.direction);
    return this.svc.getPagedData(pr).pipe(first());
  }
}
