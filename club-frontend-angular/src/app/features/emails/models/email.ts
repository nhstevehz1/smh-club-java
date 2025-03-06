import {EmailType} from "./email-type";

export interface Email {
    email: string;
    email_type: EmailType;
}

export interface EmailDetails extends Email {
    id: number;
    member_number: number;
}

export interface EmailCreate extends Email {}

export interface EmailEdit extends EmailDetails {}
