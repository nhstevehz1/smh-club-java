import {SelectOption} from "../../../shared/components/editor-form-fields/models/select-option";

export enum PhoneType {
    Home = 'Home',
    Work = 'Work',
    Mobile = 'Mobile'
}

export interface PhoneTypeOption extends SelectOption<PhoneType> {}
