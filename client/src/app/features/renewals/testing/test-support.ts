import {DateTime} from 'luxon';

import {RenewalMember} from '@app/features/renewals/models/renewal';
import {PagedData} from '@app/shared/services/api-service/models';
import {TestHelpers} from '@app/shared/testing';

export class RenewalTest {

  static generateRenewalPageData(page: number, size: number, total: number): PagedData<RenewalMember> {
    const content = this.generateList(size);
    return TestHelpers.generatePagedData(page, size, total, content);
  }
  static generateList(size: number): RenewalMember[] {
    const list: RenewalMember[] = [];

    for (let ii = 0; ii < size; ii++) {
      const renewalMember = {
        id: ii,
        member_id: ii,
        member_number: ii,
        renewal_date: DateTime.now(),
        renewal_year: DateTime.now().year,
        full_name: {
          first_name: ii + ' First',
          middle_name: ii +  ' Middle',
          last_name: ii + ' Last',
          suffix: ii + ' Suffix'
        }
      }
      list.push(renewalMember);
    }
    return list;
  }

}
