import {defer} from "rxjs";
import {PagedData} from "../models/paged-data";
import {MatFormFieldHarness} from "@angular/material/form-field/testing";
import {MatInputHarness} from "@angular/material/input/testing";
import {MatSelectHarness} from "@angular/material/select/testing";

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

export async function getFormFieldValue(harness: MatFormFieldHarness | null ): Promise<string> {

    const control = await harness?.getControl();

    if(control instanceof MatInputHarness) {

        const input: MatInputHarness = (control as MatInputHarness);
        return input.getValue();
    } else {

        const select: MatSelectHarness = (control as MatSelectHarness);
        return select.getValueText()
    }
}
