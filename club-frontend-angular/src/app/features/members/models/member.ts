import {DateTime} from "luxon";

export interface Member {
    first_name: string;
    middle_name: string;
    last_name: string;
    suffix: string;
    birth_date: DateTime;
    joined_date: DateTime;
}

export interface MemberDetails extends Member {
    id: number;
    member_number: number;
}

export interface MemberCreate extends Member {}

export interface MemberEdit extends MemberDetails {}
