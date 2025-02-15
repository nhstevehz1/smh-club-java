import {FullName} from "../../../shared/models/full-name";
import {DateTime} from "luxon";
import {Renewal} from "./renewal";

export interface RenewalMember extends Renewal{
    full_name: FullName;
}
