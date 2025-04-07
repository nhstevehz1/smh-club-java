import {DateTime} from 'luxon';

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
