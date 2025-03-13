import {Member, MemberCreate, MemberDetails} from '../models/member';
import {DateTime} from 'luxon';
import {PagedData} from '../../../shared/models/paged-data';
import {generatePagedData} from '../../../shared/test-helpers/test-helpers';

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

export function generateMemberCreate(): MemberCreate {
    const member = generateMember(1);
    return {
        member_number: member.member_number,
        first_name: member.first_name,
        middle_name: member.middle_name,
        last_name: member.last_name,
        suffix: member.suffix,
        birth_date: member.birth_date,
        joined_date: member.joined_date,
        addresses: [],
        emails: [],
        phones: []
    }
}
