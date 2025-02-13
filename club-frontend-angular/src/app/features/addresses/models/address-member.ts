import {FullName} from "../../../shared/models/full-name";
import {AddressType} from "./address-type";

export interface AddressMember {
    id: number;
    address1: string;
    address2?: string;
    city: string;
    state: string;
    zip: string;
    address_type: AddressType;
    member_number: number;
    full_name: FullName;
}
