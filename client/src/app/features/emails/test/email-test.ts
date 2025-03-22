import {PagedData} from "../../../shared/models/paged-data";
import {generatePagedData} from "../../../shared/test-helpers/test-helpers";
import {EmailBase, EmailMember, EmailDetails} from "../models/email";
import {EmailType} from "../models/email-type";
import {FormControl, FormGroup} from "@angular/forms";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";

export function generateEmailPagedData(page: number, size: number, total: number): PagedData<EmailMember> {
    const content = generateEmailList(size);
    return generatePagedData(page, size, total, content);
}

export function generateEmailList(size: number): EmailMember[] {
    const list: EmailMember[] = [];

    for (let ii = 0; ii < size; ii++) {
        const emailMember = {
            id: ii,
            member_id: ii,
            email: `${ii}email@${ii}domain.com`,
            email_type: EmailType.Home,
            full_name: {
                first_name: ii + " First",
                middle_name: ii +  " Middle",
                last_name: ii + " Last",
                suffix: ii + " Suffix"
            }
        }
        list.push(emailMember);
    }
    return list;
}

export function generateEmailCreateForm(): FormModelGroup<EmailBase> {
    return new FormGroup({
        email: new FormControl('', {nonNullable: true}),
        email_type: new FormControl<EmailType>(EmailType.Work, {nonNullable: true})
    });
}

export function generateEmailUpdate(): EmailDetails {
    return {
        id: 0,
        member_id: 3,
        email: 'email@email.com',
        email_type: EmailType.Home
    }
}
