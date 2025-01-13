import {FullName} from "../../../shared/models/full-name";
import {DateTime} from "luxon";

export interface RenewalMember {
    id: number;
    member_number: number;
    full_name: FullName;
    renewal_date: DateTime;
    renewal_year: number;
}
