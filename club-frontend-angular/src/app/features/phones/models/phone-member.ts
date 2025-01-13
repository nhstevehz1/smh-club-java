import {FullName} from "../../../shared/models/full-name";

export interface PhoneMember {
    id: number;
    member_number: number;
    full_name: FullName;
    country_code: string;
    phone_number: string;
    phone_type: string;
}
