import {DateTime} from "luxon";
import {Address} from "../../addresses/models/address";
import {EmailCreate} from "../../emails/models/email";
import {PhoneCreate} from "../../phones/models/phone";

export interface Member {
    member_number: number;
    first_name: string;
    middle_name: string;
    last_name: string;
    suffix: string;
    birth_date: DateTime;
    joined_date: DateTime;
}

export interface MemberDetails extends Member {
    id: number;
}

export interface MemberCreate extends Member {
    addresses: Address[];
    emails: EmailCreate[];
    phones: PhoneCreate[];
}

export interface MemberUpdate extends MemberDetails {}
