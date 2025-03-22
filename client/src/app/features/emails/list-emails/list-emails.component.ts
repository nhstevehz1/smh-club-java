import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {EmailMember} from "../models/email";
import {
    SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {EmailService} from "../services/email.service";
import {BaseTableComponent} from "../../../shared/components/base-table-component/base-table-component";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {EmailType} from "../models/email-type";

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

    resultsLength = 0;
    datasource = new MatTableDataSource<EmailMember>();
    columns: ColumnDef<EmailMember>[] = [];

    private emailTypeMap = new Map<EmailType, string>([
       [EmailType.Work, 'emails.type.work'],
       [EmailType.Home, 'emails.type.home'],
       [EmailType.Other, 'emails.type.other']
    ]);

    constructor(private svc: EmailService) {
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
                    this.resultsLength = data.page.totalElements;

                    // map the content array only
                    return data._content;
                })
            ).subscribe({
                // set the data source with the new page
                next: data => this.datasource.data = data!
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
