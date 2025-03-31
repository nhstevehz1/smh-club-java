import {DateTime} from "luxon";

import {PagedData} from '@app/shared/services';
import {generatePagedData} from "@app/shared/testing/test-helpers";
import {RenewalMember} from '@app/features/renewals';

export function generateRenewalPageData(page: number, size: number, total: number): PagedData<RenewalMember> {
    const content = generateRenewalList(size);
    return generatePagedData(page, size, total, content);
}

export function generateRenewalList(size: number): RenewalMember[] {
    const list: RenewalMember[] = [];

    for (let ii = 0; ii < size; ii++) {
        const renewalMember = {
            id: ii,
            member_id: ii,
            member_number: ii,
            renewal_date: DateTime.now(),
            renewal_year: DateTime.now().year,
            full_name: {
                first_name: ii + " First",
                middle_name: ii +  " Middle",
                last_name: ii + " Last",
                suffix: ii + " Suffix"
            }
        }
        list.push(renewalMember);
    }
    return list;
}
