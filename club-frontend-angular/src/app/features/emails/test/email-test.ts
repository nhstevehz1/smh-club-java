import {PagedData} from "../../../shared/models/paged-data";
import {generatePagedData} from "../../../shared/test-helpers/test-helpers";
import {EmailMember} from "../models/email";
import {EmailType} from "../models/email-type";

export function generateEmailPagedData(page: number, size: number, total: number): PagedData<EmailMember> {
    const content = generateEmailList(size);
    return generatePagedData(page, size, total, content);
}

export function generateEmailList(size: number): Array<EmailMember> {
    let list: Array<EmailMember> = [];

    for (let ii = 0; ii < size; ii++) {
        let emailMember = {
            id: ii,
            member_id: ii,
            email: `${ii}email@${ii}domain.com`,
            email_type: EmailType.Home,
            full_name: {
                first_name: ii + " First",
                middle_name: ii +  " Middle",
                last_name: ii + " Last",
                suffix: ii + " Suffix"
            }
        }
        list.push(emailMember);
    }
    return list;
}
