import {FullName} from '@app/shared/models';

export interface Address {
  id: number;
  member_id: number;
  address1: string;
  address2: string;
  city: string;
  state: string;
  postal_code: string;
  address_type: AddressType;
}

export interface AddressMember extends Address {
  full_name: FullName;
}

export enum AddressType {
  Home = 'Home',
  Work = 'Work',
  Other = 'Other'
}
