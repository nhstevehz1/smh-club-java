import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {DateTime} from 'luxon';

import {Renewal, RenewalMember} from '@app/features/renewals/models/renewal';
import {BaseApiService} from '@app/shared/services/api-service/base-api.service';
import {PageRequest, PagedData} from '@app/shared/services/api-service/models';
import {FilterByMemberService} from '@app/shared/services/api-service/filter-by-member-service';


@Injectable()
export class RenewalService extends BaseApiService<Renewal, RenewalMember> implements FilterByMemberService<Renewal> {

  constructor(http: HttpClient) {
    super('/api/v1/renewals', http);
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<RenewalMember>> {
    return super.getPagedData(pageRequest).pipe(
      map(pd => {
        if (pd && pd._content) {
          pd._content.map(r => this.castDateTimeValue(r))
        }
        return pd;
      })
    );
  }

  override create(create: Renewal): Observable<Renewal> {
    return super.create(create).pipe(
      map(data => this.castDateTimeValue(data))
    );
  }

  override update(update: Renewal): Observable<Renewal> {
    return super.update(update).pipe(
      map(data => this.castDateTimeValue(data))
    );
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }

  public getAllByMember(memberId: number): Observable<Renewal[]> {
    const uri = `/members/${memberId}/renewals`;
    return this.http.get<Renewal[]>(uri).pipe(
      map(data => data.map(address => this.castDateTimeValue(address)))
    );
  }

  private castDateTimeValue(renewal: Renewal): Renewal {
    const date = renewal.renewal_date as unknown as string;
    renewal.renewal_date = DateTime.fromISO(date);

    return renewal;
  }
}
