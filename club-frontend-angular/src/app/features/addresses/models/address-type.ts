import {SelectOption} from "../../../shared/components/editor-form-fields/models/select-option";

export enum AddressType {
    Home = 'Home',
    Work = 'Work',
    Other = 'Other'
}

export interface AddressTypeOption extends SelectOption<AddressType> {}
