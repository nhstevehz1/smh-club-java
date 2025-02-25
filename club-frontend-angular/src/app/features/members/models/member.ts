import {Address} from "../../addresses/models/address";
import {Email} from "../../emails/models/email";
import {Phone} from "../../phones/models/phone";
import {DateTime} from "luxon";

export interface Member {
    id: number;
    member_number: number;
    first_name: string;
    middle_name: string;
    last_name: string;
    suffix: string;
    birth_date: DateTime;
    joined_date: DateTime;

}

export interface MemberCreate {
    member: Member;
    addresses: Array<Address>;
    emails: Array<Email>;
    phones: Array<Phone>;
}
