export interface PagedData<T> {
    _content: T[];
    page: Page;
}

export interface Page {
    size: number;
    number: number;
    totalPages: number;
    totalElements: number;
    isFirst: boolean;
    hasPrevious: boolean;
    hasNext: boolean;
    isLast: boolean;
}
