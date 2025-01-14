import {AfterViewInit, Component, inject, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {EmailMember} from "../models/email-member";
import {
    SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {EmailService} from "../services/email.service";
import {TableComponentBase} from "../../../shared/components/table-component-base/table-component-base";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

@Component({
    selector: 'app-list-emails',
    imports: [SortablePageableTableComponent],
    providers: [EmailService],
    templateUrl: './list-emails.component.html',
    styleUrl: './list-emails.component.scss'
})
export class ListEmailsComponent
    extends TableComponentBase<EmailMember> implements OnInit, AfterViewInit {

    @ViewChild(SortablePageableTableComponent, {static: true})
    private _table!: SortablePageableTableComponent;

    private _svc = inject(EmailService);

    resultsLength = 0;
    datasource = new MatTableDataSource<EmailMember>();
    columns: ColumnDef<EmailMember>[] = [];

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
                    return this._svc.getEmails(pr)
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

    protected getColumns(): ColumnDef<EmailMember>[] {
        return [
            {
                name: 'email',
                displayName: 'Email',
                isSortable: true,
                cell:(element: EmailMember) => `${element.email}`
            },
            {
                name: 'email_type',
                displayName: 'Type',
                isSortable: true,
                cell:(element: EmailMember) => `${element.email_type}`
            },
            {
                name: 'member_number',
                displayName: 'No.',
                isSortable: true,
                cell:(element: EmailMember) => `${element.member_number}`
            },
            {
                name: 'full_name',
                displayName: 'Member',
                isSortable: true,
                cell:(element: EmailMember) => this.getFullName(element.full_name)
            }
        ];
    }

}
