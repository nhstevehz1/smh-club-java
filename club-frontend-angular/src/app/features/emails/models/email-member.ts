import {FullName} from "../../../shared/models/full-name";
import {Email} from "./email";

export interface EmailMember extends Email{

    full_name: FullName;
}
