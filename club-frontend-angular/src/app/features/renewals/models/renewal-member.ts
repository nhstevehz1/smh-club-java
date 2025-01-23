import {FullName} from "../../../shared/models/full-name";
import {DateTime} from "luxon";

export interface RenewalMember {
    id: number;
    renewal_date: DateTime;
    renewal_year: number;
    full_name: FullName;
}
