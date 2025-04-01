import { Injectable } from '@angular/core';

import {ColumnDef} from '@app/shared/components/sortable-pageable-table';
import {BaseTableService} from '@app/shared/services/table-service';

import {PhoneMember, PhoneType} from '@app/features/phones/models/phone';

@Injectable({
  providedIn: 'root'
})
export class PhoneTableService extends BaseTableService<PhoneMember> {

  constructor() {
    super();
  }

  getColumnDefs(): ColumnDef<PhoneMember>[] {
    return [{
        columnName: 'phone_number',
        displayName: 'phones.list.columns.phoneNumber',
        isSortable: true,
        cell: (element: PhoneMember) => this.getPhoneNumber(element)
      }, {
        columnName: 'phone_type',
        displayName: 'phones.list.columns.phoneType',
        isSortable: false,
        cell: (element: PhoneMember) => this.phoneTypeMap.get(element.phone_type)
      }, {
        columnName: 'full_name',
        displayName: 'phones.list.columns.fullName',
        isSortable: true,
        cell: (element: PhoneMember) => this.getFullName(element.full_name)
      },
    ];
  }

  private getPhoneNumber(phoneMember: PhoneMember): string {
    const code = phoneMember.country_code;
    const number = phoneMember.phone_number;

    // format phone number exp: (555) 555-5555
    const regex = /^(\d{3})(\d{3})(\d{4})$/;
    const formated = number.replace(regex, '($1) $2-$3');

    return `+${code} ${formated}`;
  }

  private phoneTypeMap = new Map<PhoneType, string>([
    [PhoneType.Work, 'phones.type.work'],
    [PhoneType.Home, 'phones.type.home'],
    [PhoneType.Mobile, 'phones.type.mobile']
  ]);
}
