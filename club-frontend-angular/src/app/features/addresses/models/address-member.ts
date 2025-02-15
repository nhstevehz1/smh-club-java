import {FullName} from "../../../shared/models/full-name";
import {AddressType} from "./address-type";
import {Address} from "./address";

export interface AddressMember extends Address{
    member_number: number;
    full_name: FullName;
}
