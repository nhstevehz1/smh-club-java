import {FullName} from "../../../shared/models/full-name";
import {Renewal} from "./renewal";

export interface RenewalMember extends Renewal{
    full_name: FullName;
}
