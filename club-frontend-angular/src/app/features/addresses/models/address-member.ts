import {ListModelBase} from "../../../shared/models/list-model-base";

export interface AddressMember extends ListModelBase {
    id: number;
    address1: string;
    address2: string;
    city: string;
    state: string;
    zip: string;
    address_ype: string;
}
