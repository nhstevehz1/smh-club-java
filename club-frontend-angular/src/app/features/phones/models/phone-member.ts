import {FullName} from "../../../shared/models/full-name";

export interface PhoneMember {
    id: number;
    member_number: number;
    full_name: FullName;
    phone_number: string;
    phone_type: string;
}
