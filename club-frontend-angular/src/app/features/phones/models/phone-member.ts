import {FullName} from "../../../shared/models/full-name";
import {Phone} from "./phone";

export interface PhoneMember extends Phone {
    full_name: FullName;
}
