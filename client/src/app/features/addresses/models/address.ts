import {AddressType} from "./address-type";
import {FullName} from "../../../shared/models/full-name";

export interface AddressCreate {
    address1: string;
    address2: string;
    city: string;
    state: string;
    postal_code: string;
    address_type: AddressType;
}

export interface Address extends AddressCreate {
    id: number;
    member_id: number;
}

export interface AddressMember extends Address {
    full_name: FullName;
}

//export interface AddressCreate extends Address {}

//export interface AddressUpdate extends AddressDetails {}
