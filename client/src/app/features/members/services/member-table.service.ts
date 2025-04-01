import {Injectable} from '@angular/core';
import {DateTime} from 'luxon';
import {TranslateService} from '@ngx-translate/core';

import {ColumnDef} from '@app/shared/components/sortable-pageable-table';
import {DateTimeToFormatPipe} from '@app/shared/pipes';
import {BaseTableService} from '@app/shared/services';

import {Member} from '@app/features/members/models/member';

@Injectable()
export class MemberTableService extends BaseTableService<Member>{

  constructor(private translate: TranslateService,
              private dtFormat: DateTimeToFormatPipe) {
    super();
  }

  getColumnDefs(): ColumnDef<Member>[] {
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
        cell: (element: Member) => this.contactStrings(element.first_name, element.middle_name)
      }, {
        columnName: 'last_name',
        displayName: 'members.list.columns.lastName',
        isSortable: true,
        cell: (element: Member) => this.contactStrings(element.last_name, element.suffix),
      }, {
        columnName: 'birth_date',
        displayName: 'members.list.columns.birthDate',
        isSortable: true,
        cell: (element: Member) => {
          return this.dtFormat.transform(element.birth_date, DateTime.DATE_SHORT,
            {locale: this.translate.currentLang});
        }
      }, {
        columnName: 'joined_date',
        displayName: 'members.list.columns.joinedDate',
        isSortable: true,
        cell: (element: Member) => {
          return this.dtFormat.transform(element.joined_date, DateTime.DATE_SHORT,
            {locale: this.translate.currentLang});
        }
      }
    ];
  }

  private contactStrings(str1: string, str2?: string, delimiter?: string): string {
    const val2 = str2 || '';
    const delim = delimiter || ' ';
    return str1.concat(val2, delim).trim();
  }
}
