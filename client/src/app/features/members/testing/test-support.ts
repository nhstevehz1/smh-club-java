import {FormControl, FormGroup} from '@angular/forms';
import {DateTime} from 'luxon';
import {Member} from '@app/features/members/models/member';
import {PagedData} from '@app/shared/services/api-service/models';
import {TestHelpers} from '@app/shared/testing';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';

export class MemberTest {
  static generatePagedData(page: number, size: number, total: number): PagedData<Member> {
    const content = this.generateList(size);
    return TestHelpers.generatePagedData(page, size, total, content);
  }

  static generateList(size: number): Member[] {
    const list: Member[] = [];

    for(let ii = 0; ii < size; ii++) {
      const member: Member = this.generateMember(ii);
      list.push(member);
    }
    return list;
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

  static generateColumnDefs(): ColumnDef<Member>[] {
    return [{
      columnName: 'member_number',
      displayName: 'members.list.columns.memberNumber',
      translateDisplayName: false,
      isSortable: true,
      cell: (element: Member) => `${element.member_number}`
    }, {
      columnName: 'first_name',
      displayName: 'members.list.columns.firstName',
      isSortable: true,
      cell: (element: Member) => `${element.first_name}`
    }, {
      columnName: 'last_name',
      displayName: 'members.list.columns.lastName',
      isSortable: true,
      cell: (element: Member) => `${element.last_name}`
    }, {
      columnName: 'birth_date',
      displayName: 'members.list.columns.birthDate',
      isSortable: true,
      cell: (element: Member) => `${element.birth_date}`
    }, {
      columnName: 'joined_date',
      displayName: 'members.list.columns.joinedDate',
      isSortable: true,
      cell: (element: Member) => `${element.joined_date}`
    }
    ];
  }
}
