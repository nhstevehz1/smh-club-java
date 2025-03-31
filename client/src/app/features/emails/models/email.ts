import {EmailType} from './email-type';
import {FullName} from '../../../shared/models/full-name';

export interface EmailCreate {
    email: string;
    email_type: EmailType;
}

export interface Email extends EmailCreate {
    id: number;
    member_id: number;
}

export interface EmailMember extends Email {
    full_name: FullName;
}
