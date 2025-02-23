import {EmailType} from "./email-type";

export interface Email {
    id: number;
    member_id: number;
    email: string;
    email_type: EmailType | string;
}
