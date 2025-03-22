import {DateTime} from "luxon";
import {AddressCreate} from "../../addresses/models/address";
import {EmailCreate} from "../../emails/models/email";
import {PhoneCreate} from "../../phones/models/phone";

export interface MemberBase {
    member_number: number;
    first_name: string;
    middle_name: string;
    last_name: string;
    suffix: string;
    birth_date: DateTime;
    joined_date: DateTime;
}

export interface Member extends MemberBase {
    id: number;
}

export interface MemberCreate extends MemberBase {
    addresses: AddressCreate[];
    emails: EmailCreate[];
    phones: PhoneCreate[];
}
