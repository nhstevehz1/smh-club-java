import {ColumnDef} from "../sortable-pageable-table/models/column-def";
import {SortDirection} from "@angular/material/sort";
import {PageRequest, SortDef} from "../../models/page-request";
import {FullName} from "../../models/full-name";

export abstract class TableComponentBase <T> {

    protected getPageRequest(pageIndex?: number, pageSize?: number,
                             sort?: string, direction?: SortDirection ): PageRequest {

        const pr = PageRequest.of(
            pageIndex,
            pageSize,
        )

        // The dynamic page request supports multiple sort fields.
        // currently, our implementation supports only single column sort
        if(sort !== undefined) {
            const sortDef = SortDef.of(sort, direction);
            pr.addSort(sortDef);
        }

        return  pr;
    }

    protected abstract getColumns(): ColumnDef<T>[];

    protected getFullName(fullName: FullName): string {
        const first = fullName.first_name;
        const last = fullName.last_name;
        const middle = fullName.middle_name || '';
        const suffix = fullName.suffix || '';

        const firstName = `${first} ${middle}`.trim();
        const lastName = `${last} ${suffix}`.trim();
        return `${lastName}, ${firstName}`;
    }

    protected contactStrings(str1: string, str2?: string, delimiter?: string): string {
        const val2 = str2 || '';
        const delim = delimiter || ' ';
        return str1.concat(val2, delim).trim();
    }
}
