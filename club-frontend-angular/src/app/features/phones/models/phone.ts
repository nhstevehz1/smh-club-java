import {PhoneType} from "./phone-type";
import {FullName} from "../../../shared/models/full-name";

export interface Phone {
    country_code: string;
    phone_number: string;
    phone_type: PhoneType;
}

export interface PhoneDetails extends Phone {
    id: number;
    member_id: number;
}

export interface PhoneMember extends PhoneDetails {
    full_name: FullName;
}

export interface PhoneCreate extends Phone {}

export interface PhoneUpdate extends PhoneDetails {}
