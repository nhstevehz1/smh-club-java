import {AddressType} from "./address-type";
import {FullName} from "../../../shared/models/full-name";

export interface Address {
    address1: string;
    address2: string;
    city: string;
    state: string;
    zip: string;
    address_type: AddressType;
}

export interface AddressDetails extends Address {
    id: number;
    member_number: number;
}

export interface AddressMember extends AddressDetails {
    full_name: FullName;
}

export interface AddressCreate extends Address {}

export interface AddressUpdate extends AddressDetails {}
