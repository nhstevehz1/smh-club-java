import {FullName} from "../../../shared/models/full-name";

export interface EmailMember {
    id: number;
    email: string;
    email_type: string;
    full_name: FullName;
}
