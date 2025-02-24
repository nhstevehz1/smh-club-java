import {Member, MemberCreate} from '../models/member';
import {DateTime} from 'luxon';
import {PagedData} from '../../../shared/models/paged-data';
import {generatePagedData} from '../../../shared/test-helpers/test-helpers';

export function generateMemberPageData(page: number, size: number, total: number): PagedData<Member> {
    const content = generateMemberList(size);
    return generatePagedData(page, size, total, content);
}

export function generateMemberList(size: number): Array<Member> {
    let list: Array<Member> = [];

    for(let ii = 0; ii < size; ii++) {
        let member: Member = generateMember(ii);
        list.push(member);
    }
    return list;
}

export function generateMember(prefix: number): Member {
    return {
        id: prefix,
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
    return {
        member: generateMember(1),
        addresses: [],
        emails: [],
        phones: []
    }
}
