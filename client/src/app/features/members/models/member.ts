import {DateTime} from "luxon";
import {Address} from "../../addresses/models/address";
import {Email} from "../../emails/models/email";
import {Phone} from "../../phones/models/phone";
import {Renewal} from '../../renewals/models/renewal';

export interface MemberCreate {
    member_number: number;
    first_name: string;
    middle_name: string;
    last_name: string;
    suffix: string;
    birth_date: DateTime;
    joined_date: DateTime;
}

export interface Member extends MemberCreate {
    id: number;
}

export interface MemberDetails extends Member {
    addresses: Address[];
    emails: Email[];
    phones: Phone[];
    renewals: Renewal[];
}
