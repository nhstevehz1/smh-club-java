import {AfterViewInit, Component, computed, OnInit, signal, Signal, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Email, EmailMember} from "../models/email";
import {
  SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {EmailService} from "../services/email.service";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {EmailType} from "../models/email-type";
import {MatDialog} from '@angular/material/dialog';
import {EditAction, EditDialogData, EditEvent} from '../../../shared/components/edit-dialog/models/edit-event';
import {EmailEditorComponent} from '../email-editor/email-editor.component';
import {AuthService} from '../../../core/auth/services/auth.service';
import {PermissionType} from '../../../core/auth/models/permission-type';
import {EditDialogFactoryService} from '../../../shared/components/edit-dialog/services/edit-dialog-factory.service';
import {EditDialogService} from '../../../shared/components/edit-dialog/services/edit-dialog.service';
import {EditDialogComponent} from '../../../shared/components/edit-dialog/edit-dialog.component';


@Component({
    selector: 'app-list-emails',
    imports: [SortablePageableTableComponent],
    providers: [EmailService],
    templateUrl: './list-emails.component.html',
    styleUrl: './list-emails.component.scss'
})
export class ListEmailsComponent
    extends BaseTableComponent<EmailMember> implements OnInit, AfterViewInit {

    @ViewChild(SortablePageableTableComponent, {static: true})
    private _table!: SortablePageableTableComponent<EmailMember>;

    resultsLength = signal(0);
    datasource = signal(new MatTableDataSource<EmailMember>());
    columns = signal<ColumnDef<EmailMember>[]>([]);

    hasWriteRole: Signal<boolean>

    private emailTypeMap = new Map<EmailType, string>([
       [EmailType.Work, 'emails.type.work'],
       [EmailType.Home, 'emails.type.home'],
       [EmailType.Other, 'emails.type.other']
    ]);

    constructor(private svc: EmailService,
                private auth: AuthService,
                private dialog: MatDialog) {
        super();
        this.hasWriteRole = computed(() => this.auth.hasPermission(PermissionType.write));
    }

    ngOnInit(): void {
        this.columns.update(() => this.getColumns());
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
                    return this.svc.getEmails(pr)
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
                    this.resultsLength.update(() => data.page.totalElements);
                    // map the content array only
                    return data._content;
                })
            ).subscribe({
                // set the data source with the new page
                next: data => this.datasource().data = data
            });
    }

    onEditClick(event: EditEvent<EmailMember>): void {
      this.openDialog(event, EditAction.Edit);
    }

    onDeleteClick(event: EditEvent<EmailMember>): void {
      this.openDialog(event, EditAction.Delete);
    }

    private openDialog(event: EditEvent<EmailMember>, action: EditAction): void {
      const dialogData: EditDialogData<Email> = {
        form: this.svc.generateEmailForm(),
        context: {
          id: event.data.id,
          member_id: event.data.member_id,
          email: event.data.email,
          email_type: event.data.email_type
        },
        title: 'my title',
        component: EmailEditorComponent,
        action: action
      };

      const dialogRef =
        this.dialog.open<EditDialogComponent<Email>, EditDialogData<Email> >(
          EditDialogComponent<Email>, {data: dialogData}
        );

      dialogRef.afterClosed().subscribe({
        next: (result: EditDialogData<Email>) =>  {
          if(result.action == EditAction.Edit) {
            const emailMember = result.context as EmailMember;
            emailMember.full_name = event.data.full_name;

            const data = this.datasource().data;
            data[event.idx] = emailMember;
            this.datasource().data = data;
            console.debug('data at idx', event.idx, this.datasource().data[event.idx])
          } else if(result.action == EditAction.Delete) {
            this.datasource().data.slice(event.idx, 1);
          }
        }
      });
    }

    protected getColumns(): ColumnDef<EmailMember>[] {
        return [
            {
                columnName: 'email',
                displayName: 'emails.list.columns.email',
                isSortable: true,
                cell:(element: EmailMember) => `${element.email}`
            },
            {
                columnName: 'email_type',
                displayName: 'emails.list.columns.emailType',
                isSortable: false,
                cell:(element: EmailMember) => this.emailTypeMap.get(element.email_type)
            },
            {
                columnName: 'full_name',
                displayName: 'emails.list.columns.fullName',
                isSortable: true,
                cell:(element: EmailMember) => this.getFullName(element.full_name)
            }
        ];
    }

}
