import {SortDirection} from '@angular/material/sort';

export class PageRequest {
    page: number | undefined;
    size: number | undefined;
    sorts: SortDef[] = [];

    private constructor(page?: number, size?: number) {
        this.page = page;
        this.size = size;
    }

    public addSort(sort: SortDef ): void {
        this.sorts.push(sort);
    }

    public createQuery(): string | null {
        let query = null;

        query = this.getPageQuery(query);
        query = this.getSizeQuery(query);
        query = this.getSortQuery(query);

        return query;
    }

    public static of(page?: number, size?: number ): PageRequest {
        return new PageRequest(page, size);
    }

    public static empty(): PageRequest {
        return new PageRequest(undefined, undefined);
    }

    private getPageQuery(query: string | null): string | null {
        if (this.page !== undefined) {
            return query == null ? `?page=${this.page}` : query + `&page=${this.page}`;
        }
        return query;
    }

    private getSizeQuery(query: string | null): string | null {
        if (this.size !== undefined) {
            return query == null ? `?size=${this.size}` : query + `&size=${this.size}`;
        }
        return query;
    }

    private getSortQuery(query: string | null): string | null {
        if (this.sorts.length > 0) {
            query = query == null ? '?': query + '&';
        }

        for (let ii = 0; ii < this.sorts.length; ii++) {
            query = ii == 0
                ? query + `${this.sorts[ii].createQuery()}`
                : query + `&${this.sorts[ii].createQuery()}`;
        }
        return query;
    }
}

export class SortDef {
    sort: string;
    direction: SortDirection | undefined;

    private constructor(sort: string, direction?: SortDirection) {
        this.sort = sort;
        this.direction = direction;
    }

    public createQuery(): string {
        let query = `sort=${this.sort}`;
        if (this.direction !== undefined) {
            query = query + `,${this.direction}`;
        }
        return query;
    }

    public static of(sort: string, direction?: SortDirection): SortDef {
        return new SortDef(sort, direction);
    }

}
