import {PagedData} from "../../../shared/models/paged-data";
import {generatePagedData} from "../../../shared/test-helpers/test-helpers";
import {PhoneMember} from "../models/phone-member";

export function generatePhonePageData(page: number, size: number, total: number): PagedData<PhoneMember> {
    const content = generatePhoneList(size);
    return generatePagedData(page, size, total, content);
}

export function generatePhoneList(size: number): Array<PhoneMember> {
    let list: Array<PhoneMember> = [];

    for(let ii = 0; ii < size; ii++) {
        let phone: PhoneMember = {
            id: ii,
            member_id: ii,
            country_code: '1',
            phone_number: `60388399${ii}`,
            phone_type: 'Home',
            full_name: {
                first_name: ii + " First",
                middle_name: ii +  " Middle",
                last_name: ii + " Last",
                suffix: ii + " Suffix"
            }
        }
        list.push(phone);
    }

    return list;
}
