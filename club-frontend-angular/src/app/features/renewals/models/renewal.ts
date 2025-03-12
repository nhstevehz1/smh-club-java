import {DateTime} from "luxon";
import {FullName} from "../../../shared/models/full-name";

export interface Renewal {
    renewal_date: DateTime;
    renewal_year: number;
}

export interface RenewalDetails extends Renewal {
    id: number;
    member_id: number;
}

export interface RenewalMember extends RenewalDetails {
    full_name: FullName
}

export interface RenewalCreate extends Renewal {}

export interface RenewalCreate extends RenewalDetails {}
