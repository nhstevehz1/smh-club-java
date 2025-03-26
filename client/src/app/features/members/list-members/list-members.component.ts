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
import {TranslateService} from "@ngx-translate/core";
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
              private dialog: MatDialog,
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

  onEditClick(event: EditEvent<Member>): void {
    this.openDialog(event, EditAction.Edit);
  }

  onDeleteClick(event: EditEvent<Member>): void {
    this.openDialog(event, EditAction.Delete)
  }

  private getCurrentPage(): Observable<PagedData<Member>> {
    // assemble the dynamic page request
    const pr = this.getPageRequest(
      this._table.paginator.pageIndex, this._table.paginator.pageSize,
      this._table.sort.active, this._table.sort.direction);
    return this.svc.getMembers(pr).pipe(first());
  }

  private openDialog(event: EditEvent<Member>, action: EditAction): void {
    const dialogData: EditDialogData<Member> = {
      title: 'Member title',
      component: MemberEditorComponent,
      form: this.svc.generateMemberForm(),
      context: event.data,
      action: action
    }

    const dialogRef =
      this.dialog.open<EditDialogComponent<Member>, EditDialogData<Member>>(
        EditDialogComponent<Member>, {data: dialogData});

    dialogRef.afterClosed().subscribe({
      next: (result: EditDialogData<Member>) => {
        if(result.action == EditAction.Edit) {
          this.updateMember(result.context);
        } else if(result.action == EditAction.Delete) {
          this.deleteMember(event.data.id);
        }
      }
    })
  }

  private updateMember(member: Member): void {
    this.svc.updateMember(member).subscribe({
      next: () => this.getCurrentPage().subscribe({
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse) => this.processRequestError(err)
      }),
      error: (err: HttpErrorResponse)=> this.processRequestError(err)
    })
  }

  private deleteMember(id: number): void {
    this.svc.deleteMember(id).subscribe({
      next: () => this.getCurrentPage().subscribe({
        next: data => this.processPageData(data),
        error: (err: HttpErrorResponse)=> this.processRequestError(err)
      }),
      error: (err: HttpErrorResponse) => this.processRequestError(err)
    })
  }
}
