import {PhoneType} from "./phone-type";

export interface Phone {
    id: number;
    member_id: number;
    country_code: string;
    phone_number: string;
    phone_type: PhoneType;
}
