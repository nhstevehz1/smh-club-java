import {BaseTableComponent} from "./base-table-component";
import {ColumnDef} from "../sortable-pageable-table/models/column-def";
import {PageRequest} from "../../models/page-request";
import {SortDirection} from "@angular/material/sort";
import {FullName} from "../../models/full-name";

export class BaseTestTableComponent<T> extends BaseTableComponent<T> {

    public getPageRequestExternal(pageIndex?: number, pageSize?: number,
                                  sort?: string, direction?: SortDirection ): PageRequest {

        return this.getPageRequest(pageIndex, pageSize, sort, direction);
    }

    public getFullNameExternal(fullName: FullName): string {
        return this.getFullName(fullName);
    }

    protected getColumns(): ColumnDef<T>[] {
        return [];
    }

}
