import {DateTime} from "luxon";
import {FullName} from "../../../shared/models/full-name";

export interface RenewalCreate {
    renewal_date: DateTime;
    renewal_year: number;
}

export interface Renewal extends RenewalCreate {
    id: number;
    member_id: number;
}

export interface RenewalMember extends Renewal {
    full_name: FullName
}
