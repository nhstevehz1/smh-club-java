import {Injectable} from '@angular/core';
import {BaseTableService} from '../../../shared/components/sortable-pageable-table/services';
import {EmailMember, EmailType} from '../models';
import {ColumnDef} from '../../../shared/components/sortable-pageable-table/models';

@Injectable()
export class EmailTableService extends BaseTableService<EmailMember> {

  constructor() {
    super();
  }

  getColumnDefs(): ColumnDef<EmailMember>[] {
    return [{
      columnName: 'email',
      displayName: 'emails.list.columns.email',
      isSortable: true,
      cell:(element: EmailMember) => `${element.email}`
    }, {
      columnName: 'email_type',
      displayName: 'emails.list.columns.emailType',
      isSortable: false,
      cell:(element: EmailMember) => this.emailTypeMap.get(element.email_type)
    }, {
      columnName: 'full_name',
      displayName: 'emails.list.columns.fullName',
      isSortable: true,
      cell:(element: EmailMember) => this.getFullName(element.full_name)
    }];
  }

  private emailTypeMap = new Map<EmailType, string>([
    [EmailType.Work, 'emails.type.work'],
    [EmailType.Home, 'emails.type.home'],
    [EmailType.Other, 'emails.type.other']
  ]);
}
