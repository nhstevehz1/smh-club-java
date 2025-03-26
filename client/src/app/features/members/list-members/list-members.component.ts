import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {MembersService} from "../services/members.service";
import {first, merge, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {Router} from "@angular/router";
import {MatTooltip} from "@angular/material/tooltip";
import {Member} from "../models/member";
import {DateTimeToFormatPipe} from "../../../shared/pipes/luxon/date-time-to-format.pipe";
import {AuthService} from "../../../core/auth/services/auth.service";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {HttpErrorResponse} from '@angular/common/http';
import {EditAction, EditDialogData, EditEvent} from '../../../shared/components/edit-dialog/models/edit-event';
import {PagedData} from '../../../shared/models/paged-data';
import {MemberEditorComponent} from '../member-editor/member-editor.component';
import {MatDialog} from '@angular/material/dialog';
import {EditDialogComponent} from '../../../shared/components/edit-dialog/edit-dialog.component';

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
      MembersService,
      TranslateService,
      DateTimeToFormatPipe
    ],
  templateUrl: './list-members.component.html',
  styleUrl: './list-members.component.scss'
})
export class ListMembersComponent extends BaseTableComponent<Member> implements AfterViewInit{

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<Member>;

  constructor(private svc: MembersService,
              auth: AuthService,
              private router: Router) {

    super(auth);
    this.columns.set(this.svc.getColumnDefs());
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
    return this.svc.getMembers(pr).pipe(first());
  }
}
