import {PagedData} from "../../../shared/models/paged-data";
import {generatePagedData} from "../../../shared/test-helpers/test-helpers";
import {RenewalMember} from "../models/renewal";
import {DateTime} from "luxon";

export function generateRenewalPageData(page: number, size: number, total: number): PagedData<RenewalMember> {
    const content = generateRenewalList(size);
    return generatePagedData(page, size, total, content);
}

export function generateRenewalList(size: number): Array<RenewalMember> {
    let list: Array<RenewalMember> = [];

    for (let ii = 0; ii < size; ii++) {
        let renewalMember = {
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
