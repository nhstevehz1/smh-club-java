import {DateTime} from "luxon";
import {Address} from '@app/features/addresses';
import {Email} from '@app/features/emails';
import {Phone} from '@app/features/phones';
import {Renewal} from '@app/features/renewals';


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
