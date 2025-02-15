import {AddressType} from "./address-type";
import {FormControl} from "@angular/forms";

export interface Address {
    id: number;
    member_id: number;
    address1: string;
    address2: string;
    city: string;
    state: string;
    zip: string;
    address_type: FormControl<AddressType | null>;
}
