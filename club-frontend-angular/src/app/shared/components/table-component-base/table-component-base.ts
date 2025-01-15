import {ColumnDef} from "../sortable-pageable-table/models/column-def";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort, Sort, SortDirection} from "@angular/material/sort";
import {PageRequest, SortDef} from "../../models/page-request";
import {FullName} from "../../models/full-name";

export abstract class TableComponentBase <Type> {

    protected getPageRequest(pageIndex?: number, pageSize?: number,
                             sort?: string, direction?: SortDirection ): PageRequest {

        let pr = PageRequest.of(
            pageIndex,
            pageSize,
        )

        // The dynamic page request supports multiple sort fields.
        // currently, our implementation supports only single column sort
        if(sort !== undefined) {
            let sortDef = SortDef.of(sort, direction);
            pr.addSort(sortDef);
        }

        return  pr;
    }

    protected abstract getColumns(): ColumnDef<Type>[];

    protected getFullName(fullName: FullName): string {
        const first = fullName.first_name;
        const last = fullName.last_name;
        const middle = fullName.middle_name || '';
        const suffix = fullName.suffix || '';

        const firstName = `${first} ${middle}`.trim();
        const lastName = `${last} ${suffix}`.trim();
        return `${lastName}, ${firstName}`;
    }
}
