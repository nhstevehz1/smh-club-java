import {FullName} from "../../../shared/models/full-name";

export interface EmailMember {
    id: number;
    member_number: number;
    full_name: FullName;
    email: string;
    email_type: string;
}
