import {SortDirection} from "@angular/material/sort";

export interface SortPageEvent {
    pageIndex: number;
    pageSize: number;
    sortActive: string;
    sortDirection: SortDirection;

}
