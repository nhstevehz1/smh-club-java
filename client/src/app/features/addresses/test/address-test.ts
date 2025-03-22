import {AddressCreate, AddressMember, AddressUpdate} from "../models/address";
import {PagedData} from "../../../shared/models/paged-data";
import {generatePagedData} from "../../../shared/test-helpers/test-helpers";
import {AddressType} from "../models/address-type";
import {FormControl, FormGroup} from "@angular/forms";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";

export function generateAddressPagedData(page: number, size: number, total: number): PagedData<AddressMember> {
    const content = generateAddressList(size);
    return generatePagedData(page, size, total, content);
}

export function generateAddressList(size: number): AddressMember[] {
    const list: AddressMember[] = [];

    for (let ii = 0; ii < size; ii++) {
        const addressMember: AddressMember = {
            id: ii,
            member_id: ii,
            address1: ii + " Street",
            address2: ii + " Apt.",
            city: ii + " City",
            state: ii + " State",
            postal_code: ii + "Zip",
            address_type: AddressType.Home,
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

export function generateAddressCreateForm(): FormModelGroup<AddressCreate> {
    return new FormGroup({
        address1: new FormControl('', {nonNullable: true}),
        address2: new FormControl('', {nonNullable: true}),
        city: new FormControl('', {nonNullable: true}),
        state: new FormControl('', {nonNullable: true}),
        postal_code: new FormControl('', {nonNullable: true}),
        address_type: new FormControl<AddressType>(AddressType.Home, {nonNullable: true})
    });
}

export function generateAddressUpdate(): AddressUpdate {
    return {
        id: 1,
        member_id: 3,
        address1: 'address1',
        address2: 'address2',
        city: 'city',
        state: 'st',
        postal_code: '12345',
        address_type: AddressType.Home
    }
}
