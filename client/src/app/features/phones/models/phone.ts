import {FullName} from '@app/shared/models';

export interface PhoneCreate {
    country_code: string;
    phone_number: string;
    phone_type: PhoneType;
}

export interface Phone extends PhoneCreate {
    id: number;
    member_id: number;
}

export interface PhoneMember extends Phone {
    full_name: FullName;
}

export enum PhoneType {
  Home = 'Home',
  Work = 'Work',
  Mobile = 'Mobile'
}
