import {Member, MemberDetails, MemberUpdate} from '../models/member';
import {DateTime} from 'luxon';
import {PagedData} from '../../../shared/models/paged-data';
import {generatePagedData} from '../../../shared/test-helpers/test-helpers';
import {FormArray, FormControl, FormGroup} from "@angular/forms";
import {generateAddressCreateForm} from "../../addresses/test/address-test";
import {generateEmailCreateForm} from "../../emails/test/email-test";
import {generatePhoneCreateForm} from "../../phones/test/phone-test";

export function generateMemberPageData(page: number, size: number, total: number): PagedData<MemberDetails> {
    const content = generateMemberList(size);
    return generatePagedData(page, size, total, content);
}

export function generateMemberList(size: number): Array<MemberDetails> {
    let list: Array<MemberDetails> = [];

    for(let ii = 0; ii < size; ii++) {
        let member: MemberDetails = generateMemberDetails(ii);
        list.push(member);
    }
    return list;
}

export function generateMemberDetails(prefix: number): MemberDetails {
    const member = generateMember(prefix);

    const str = JSON.stringify(member);

    const details: MemberDetails = JSON.parse(str);
    details.id = prefix;
    details.member_number = prefix;
    return details;
}

export function generateMember(prefix: number): Member {
    return {
        member_number: prefix,
        first_name: prefix + ' First',
        middle_name: prefix +  ' Middle',
        last_name: prefix + ' Last',
        suffix: prefix + ' Suffix',
        birth_date: DateTime.now(),
        joined_date: DateTime.now(),
    }
}

export function generateMemberCreateForm(): FormGroup {
    return new FormGroup({
        member_number: new FormControl(0, {nonNullable: true}),
       first_name: new FormControl('', {nonNullable: true}),
       middle_name: new FormControl('', {nonNullable: true}),
       last_name: new FormControl('', {nonNullable: true}),
       suffix: new FormControl('', {nonNullable: true}),
       birth_date: new FormControl<DateTime>(DateTime.now(), {nonNullable: true}),
       joined_date: new FormControl<DateTime>(DateTime.now(), {nonNullable: true}),
       addresses: new FormArray([generateAddressCreateForm()]),
       emails: new FormArray([generateEmailCreateForm()]),
       phones: new FormArray([generatePhoneCreateForm()])
    });
}

export function generateMemberUpdate(): MemberUpdate {
    const member = generateMember(1);
    return {
        id: 0,
        member_number: member.member_number,
        first_name: member.first_name,
        middle_name: member.middle_name,
        last_name: member.last_name,
        suffix: member.suffix,
        birth_date: member.birth_date,
        joined_date: member.joined_date,
    }
}
