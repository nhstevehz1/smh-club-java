import {FormControl, FormGroup} from '@angular/forms';
import {DateTime} from 'luxon';
import {Member} from '@app/features/members/models/member';
import {PagedData} from '@app/shared/services/api-service/models';
import {TestHelpers} from '@app/shared/testing';

export class MemberTest {
  static generatePagedData(page: number, size: number, total: number): PagedData<Member> {
    const content = this.generateMemberList(size);
    return TestHelpers.generatePagedData(page, size, total, content);
  }

  static generateMemberList(size: number): Member[] {
    const list: Member[] = [];

    for(let ii = 0; ii < size; ii++) {
      const member: Member = this.generateMemberDetails(ii);
      list.push(member);
    }
    return list;
  }

  static generateMemberDetails(prefix: number): Member {
    const member = this.generateMember(prefix);

    const str = JSON.stringify(member);

    const details: Member = JSON.parse(str);
    details.id = prefix;
    details.member_number = prefix;
    return details;
  }

  static generateMember(prefix: number): Member {
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

  static generateForm(): FormGroup {
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

  static generateMemberUpdate(): Member {
    const member = this.generateMember(1);
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
}
