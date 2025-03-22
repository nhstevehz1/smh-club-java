import {PhoneType} from "./phone-type";
import {FullName} from "../../../shared/models/full-name";

export interface PhoneCreate {
    country_code: string;
    phone_number: string;
    phone_type: PhoneType;
}

export interface Phone extends PhoneCreate {
    id: number;
    member_id: number;
}

export interface PhoneMember extends Phone {
    full_name: FullName;
}
