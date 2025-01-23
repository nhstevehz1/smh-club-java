import {ListModelBase} from "../../../shared/models/list-model-base";
import {FullName} from "../../../shared/models/full-name";

export interface AddressMember extends ListModelBase {
    id: number;
    address1: string;
    address2: string;
    city: string;
    state: string;
    zip: string;
    address_type: string;
    member_number: number;
    full_name: FullName;
}
