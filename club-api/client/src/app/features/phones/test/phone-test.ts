import {PagedData} from "../../../shared/models/paged-data";
import {generatePagedData} from "../../../shared/test-helpers/test-helpers";
import {PhoneCreate, PhoneMember, PhoneUpdate} from "../models/phone";
import {PhoneType} from "../models/phone-type";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {FormControl, FormGroup} from "@angular/forms";

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
            phone_type: PhoneType.Home,
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

export function generatePhoneCreateForm(): FormModelGroup<PhoneCreate> {
    return new FormGroup({
       country_code: new FormControl('', {nonNullable: true}),
       phone_number: new FormControl('', {nonNullable: true}),
       phone_type: new FormControl<PhoneType>(PhoneType.Mobile, {nonNullable: true})
    });
}

export function generatePhoneUpdate(): PhoneUpdate {
    return {
        id: 0,
        member_id: 3,
        country_code: '1',
        phone_number: '555-555-5555',
        phone_type: PhoneType.Mobile
    }
}
