import {FormControl, FormGroup} from '@angular/forms';
import {DateTime} from 'luxon';

import {generatePagedData} from '@app/shared/testing/test-helpers';
import {PagedData} from '@app/shared/services';

import {Member} from '@app/features/members';

export function generateMemberPageData(page: number, size: number, total: number): PagedData<Member> {
  const content = generateMemberList(size);
  return generatePagedData(page, size, total, content);
}

export function generateMemberList(size: number): Member[] {
  const list: Member[] = [];

  for(let ii = 0; ii < size; ii++) {
    const member: Member = generateMemberDetails(ii);
    list.push(member);
  }
  return list;
}

export function generateMemberDetails(prefix: number): Member {
  const member = generateMember(prefix);

  const str = JSON.stringify(member);

  const details: Member = JSON.parse(str);
  details.id = prefix;
  details.member_number = prefix;
  return details;
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

export function generateMemberCreateForm(): FormGroup {
  return new FormGroup({
    member_number: new FormControl(0, {nonNullable: true}),
    first_name: new FormControl('', {nonNullable: true}),
    middle_name: new FormControl('', {nonNullable: true}),
    last_name: new FormControl('', {nonNullable: true}),
    suffix: new FormControl('', {nonNullable: true}),
    birth_date: new FormControl<DateTime>(DateTime.now(), {nonNullable: true}),
    joined_date: new FormControl<DateTime>(DateTime.now(), {nonNullable: true})
  });
}

export function generateMemberUpdate(): Member {
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
