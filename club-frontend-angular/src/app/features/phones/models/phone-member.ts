import {FullName} from "../../../shared/models/full-name";

export interface PhoneMember {
    id: number;
    country_code: string;
    phone_number: string;
    phone_type: string;
    full_name: FullName;
}
