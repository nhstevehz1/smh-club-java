import {DateTime} from "luxon";

export interface Renewal {
    id: number;
    member_id: number;
    renewal_date: DateTime;
    renewal_year: number;
}
