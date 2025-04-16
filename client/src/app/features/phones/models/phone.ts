import {FullName} from '@app/shared/models';

export interface Phone {
  id: number;
  member_id: number;
  country_code: string;
  phone_number: string;
  phone_type: PhoneType;
}

export interface PhoneMember extends Phone {
    full_name: FullName;
}

export enum PhoneType {
  Home = 'Home',
  Work = 'Work',
  Mobile = 'Mobile'
}
