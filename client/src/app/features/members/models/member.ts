import {DateTime} from 'luxon';
import {Address} from '@app/features/addresses/models/address';
import {Email} from '@app/features/emails/models/email';
import {Phone} from '@app/features/phones/models/phone';
import {Renewal} from '@app/features/renewals/models/renewal';

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
