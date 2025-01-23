import {defer} from "rxjs";
import {PagedData} from "../models/paged-data";

export function asyncData<T>(data: T) {
    return defer(() => Promise.resolve(data));
}

export function generatePagedData<T>(page: number, size: number, total: number, content: Array<T>): PagedData<T> {
    const totalPages = Math.ceil(total / total);
    const hasNextPage = totalPages > (page + 1);
    const isLastPage = totalPages === (page + 1);

    return {
        _content: content,
        page: {
            size: size,
            number: page,
            totalPages: totalPages,
            totalElements: total,
            isFirst: page === 0,
            hasPrevious: !(page === 0),
            hasNext: hasNextPage,
            isLast: isLastPage
        }
    }
}
