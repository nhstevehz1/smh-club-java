import {SelectOption} from "../../../shared/components/editor-form-fields/models/select-option";

export enum EmailType {
    Home = 'Home',
    Work = 'Work',
    Other = 'Other'
}

export interface EmailTypeOption extends SelectOption<EmailType>{}
