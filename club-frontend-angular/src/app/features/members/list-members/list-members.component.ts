import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {
    SortablePageableTableComponent
} from "../../../shared/components/sortable-pageable-table/sortable-pageable-table.component";
import {MatTableDataSource} from "@angular/material/table";
import {Member} from "../models/Member";
import {ColumnDef} from "../../../shared/components/sortable-pageable-table/models/column-def";
import {MembersService} from "../services/members.service";
import {merge, of as observableOf} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {TableComponentBase} from "../../../shared/components/table-component-base/table-component-base";

@Component({
  selector: 'app-list-members',
    imports: [
        SortablePageableTableComponent
    ],
    providers: [MembersService, MembersService],
  templateUrl: './list-members.component.html',
  styleUrl: './list-members.component.scss'
})
export class ListMembersComponent
    extends TableComponentBase<Member> implements OnInit, AfterViewInit{

    @ViewChild(SortablePageableTableComponent, {static: true})
    private _table!: SortablePageableTableComponent;

    resultsLength = 0;
    datasource = new MatTableDataSource<Member>();
    columns: ColumnDef<Member>[] = [];

    constructor(private svc: MembersService) {
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
            ).subscribe(data => this.datasource.data = data!); // set the data source with the new page
    }

    /*private getPageRequest(): PageRequest {
        let page = this.table.paginator?.pageIndex;
        let size = this.table.paginator?.pageSize;

        let pr = PageRequest.of(
            page,
            size,
        )

        // The dynamic page request supports multiple sort fields.
        // currently, our implementation supports only single column sort
        if(this.table.sort.active !== undefined) {
            let sort = SortDef.of(this.table.sort.active, this.table.sort.direction);
            pr.addSort(sort);
        }
        return  pr;
    }*/

    // assemble the column defs which will be consumed by the pageable sortable table component
    protected getColumns(): ColumnDef<Member>[] {
        return [
            {
                name: 'member_number',
                displayName: 'No.',
                isSortable: true,
                cell: (element: Member) => `${element.member_number}`},
            {
                name: 'first_name',
                displayName: 'First',
                isSortable: true,
                cell: (element: Member) => `${element.first_name} ${element.middle_name}`
            },
            {
                name: 'last_name',
                displayName: 'Last',
                isSortable: true,
                cell: (element: Member) => `${element.last_name} ${element.suffix}`,
            },
            {
                name: 'birth_date',
                displayName: 'Birth Date',
                isSortable: true,
                cell: (element: Member) => `${element.birth_date}`
            },
            {
                name: 'joined_date',
                displayName: 'Joined',
                isSortable: true,
                cell: (element: Member) => `${element.joined_date}`
            }
        ];
    }
}
