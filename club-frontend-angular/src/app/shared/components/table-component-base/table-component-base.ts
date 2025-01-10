import {ColumnDef} from "../sortable-pageable-table/models/column-def";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {PageRequest, Sort} from "../../models/page-request";

export abstract class TableComponentBase <Type> {

    protected getPageRequest(paginator: MatPaginator, sort: MatSort) {
        let page = paginator.pageIndex;
        let size = paginator.pageSize;

        let pr = PageRequest.of(
            page,
            size,
        )

        // The dynamic page request supports multiple sort fields.
        // currently, our implementation supports only single column sort
        if(sort.active !== undefined) {
            let sortDef = Sort.of(sort.active, sort.direction);
            pr.addSort(sortDef);
        }
        return  pr;
    }

    protected abstract getColumns(): ColumnDef<Type>[];
}
