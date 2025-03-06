import {PhoneType} from "./phone-type";
import {FullName} from "../../../shared/models/full-name";
import {AddressDetails} from "../../addresses/models/address";

export interface Phone {
    country_code: string;
    phone_number: string;
    phone_type: PhoneType;
}

export interface PhoneDetails extends Phone {
    id: number;
    member_number: number;
}

export interface PhoneMember extends PhoneDetails {
    full_name: FullName;
}

export interface PhoneCreate extends Phone {}

export interface PhoneEdit extends AddressDetails {}
