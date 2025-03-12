import {EmailType} from "./email-type";
import {FullName} from "../../../shared/models/full-name";

export interface Email {
    email: string;
    email_type: EmailType;
}

export interface EmailDetails extends Email {
    id: number;
    member_number: number;
}

export interface EmailMember extends EmailDetails {
    full_name: FullName;
}

export interface EmailCreate extends Email {}

export interface EmailUpdate extends EmailDetails {}
