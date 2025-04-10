import {FullName} from '@app/shared/models';

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

export enum EmailType {
  Home = 'Home',
  Work = 'Work',
  Other = 'Other'
}
