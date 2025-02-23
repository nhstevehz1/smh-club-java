import {Member} from "../models/member";
import {DateTime} from "luxon";
import {PagedData} from "../../../shared/models/paged-data";
import {generatePagedData} from "../../../shared/test-helpers/test-helpers";

export function generateMemberPageData(page: number, size: number, total: number): PagedData<Member> {
    const content = generateMemberList(size);
    return generatePagedData(page, size, total, content);
}

export function generateMemberList(size: number): Array<Member> {
    let list: Array<Member> = [];

    for(let ii = 0; ii < size; ii++) {
        let member: Member = {
            id: ii,
            member_number: ii,
            first_name: ii + " First",
            middle_name: ii +  " Middle",
            last_name: ii + " Last",
            suffix: ii + " Suffix",
            birth_date: DateTime.now().toFormat('SHORT_DTE'),
            joined_date: DateTime.now().toFormat('SHORT_DATE'),
        }
        list.push(member);
    }

    return list;
}
