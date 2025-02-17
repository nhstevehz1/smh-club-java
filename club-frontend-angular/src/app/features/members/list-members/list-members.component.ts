import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {
    SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {MatTableDataSource} from "@angular/material/table";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {MembersService} from "../services/members.service";
import {merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {TableComponentBase} from "../../../shared/components/table-component-base/table-component-base";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {Router} from "@angular/router";
import {MatTooltip} from "@angular/material/tooltip";
import {Member} from "../models/member";
import {DateTime} from "luxon";
import {DateTimeToLocalPipe} from "../../../shared/pipes/luxon/date-time-to-local.pipe";
import {DateTimeToFormatPipe} from "../../../shared/pipes/luxon/date-time-to-format.pipe";
import {DateTimeFromIsoPipe} from "../../../shared/pipes/luxon/date-time-from-iso.pipe";

@Component({
  selector: 'app-list-members',
    imports: [
        SortablePageableTableComponent,
        MatIconModule,
        MatButtonModule,
        MatTooltip
    ],
    providers: [
        MembersService,
        DateTimeFromIsoPipe,
        DateTimeToFormatPipe
    ],
  templateUrl: './list-members.component.html',
  styleUrl: './list-members.component.scss'
})
export class ListMembersComponent extends TableComponentBase<Member> implements OnInit, AfterViewInit{
    @ViewChild(SortablePageableTableComponent, {static: true})
    private _table!: SortablePageableTableComponent;

    resultsLength = 0;
    datasource = new MatTableDataSource<Member>();
    columns: ColumnDef<Member>[] = [];

    constructor(private svc: MembersService,
                private router: Router,
                private dtFromIsoPipe: DateTimeFromIsoPipe,
                private dtToFormatPipe: DateTimeToFormatPipe) {
        super();
    }

    ngOnInit(): void {
       this.columns = this.getColumns(); // create column defs
    }

    ngAfterViewInit(): void {
        merge(this._table.sort.sortChange, this._table.paginator.page)
            .pipe(
                startWith({}),
                switchMap(() => {
                    // assemble the dynamic page request
                    let pr = this.getPageRequest(
                        this._table.paginator.pageIndex, this._table.paginator.pageSize,
                        this._table.sort.active, this._table.sort.direction);

                    // pipe any errors to an Observable of null
                    return this.svc.getMembers(pr)
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
                // set the data source to the new page
                next: data => this.datasource.data = data!
            });
    }

    addMemberHandler(): void {
        this.router.navigate(['p/members/add']).then(() => {});
    }

    // assemble the column defs which will be consumed by the pageable sortable table component
    protected getColumns(): ColumnDef<Member>[] {
        return [
            {
                columnName: 'member_number',
                displayName: 'No.',
                isSortable: true,
                cell: (element: Member) => `${element.member_number}`},
            {
                columnName: 'first_name',
                displayName: 'First',
                isSortable: true,
                cell: (element: Member) => this.contactStrings(element.first_name, element.middle_name)
            },
            {
                columnName: 'last_name',
                displayName: 'Last',
                isSortable: true,
                cell: (element: Member) => this.contactStrings(element.last_name, element.suffix),
            },
            {
                columnName: 'birth_date',
                displayName: 'Birth Date',
                isSortable: true,
                cell: (element: Member) => this.transformDate(element.birth_date)
            },
            {
                columnName: 'joined_date',
                displayName: 'Joined',
                isSortable: true,
                cell: (element: Member) => this.transformDate(element.joined_date)
            }
        ];
    }

    private transformDate(dateTime: string): string | null {
        const iso = DateTime.fromISO(dateTime);
        return this.dtToFormatPipe.transform(iso, DateTime.DATE_SHORT);
    }
}
