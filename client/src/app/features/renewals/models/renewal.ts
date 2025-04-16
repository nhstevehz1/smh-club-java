import {DateTime} from 'luxon';
import {FullName} from '@app/shared/models';

export interface Renewal {
  id: number;
  member_id: number;
  renewal_date: DateTime;
  renewal_year: number;
}

export interface RenewalMember extends Renewal {
    full_name: FullName
}
