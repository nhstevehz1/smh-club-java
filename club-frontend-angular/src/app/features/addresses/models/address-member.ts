import {FullName} from "../../../shared/models/full-name";
import {Address} from "./address";

export interface AddressMember extends Address{
    member_number: number;
    full_name: FullName;
}
