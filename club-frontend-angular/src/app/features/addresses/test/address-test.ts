import {AddressMember} from "../models/address-member";
import {PagedData} from "../../../shared/models/paged-data";
import {generatePagedData} from "../../../shared/test-helpers/test-helpers";


export function generateAddressPagedData(page: number, size: number, total: number): PagedData<AddressMember> {
    const content = generateAddressList(size);
    return generatePagedData(page, size, total, content);
}

export function generateAddressList(size: number): Array<AddressMember> {
    let list: Array<AddressMember> = [];

    for (let ii = 0; ii < size; ii++) {
        let addressMember = {
            id: ii,
            member_id: ii,
            address1: ii + " Street",
            address2: ii + " Apt.",
            city: ii + " City",
            state: ii + " State",
            zip: ii + " Zip",
            address_type: "Home",
            member_number: ii,
            full_name: {
                first_name: ii + " First",
                middle_name: ii +  " Middle",
                last_name: ii + " Last",
                suffix: ii + " Suffix"
            }
        }
        list.push(addressMember);
    }
    return list;
}
