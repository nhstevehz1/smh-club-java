import {FullName} from '@app/shared/models';

export interface Email {
  id: number;
  member_id: number;
  email: string;
  email_type: EmailType;
}

export interface EmailMember extends Email {
  full_name: FullName;
}

export enum EmailType {
  Home = 'Home',
  Work = 'Work',
  Other = 'Other'
}
