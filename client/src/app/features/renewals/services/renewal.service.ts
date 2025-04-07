import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {DateTime} from 'luxon';

import {Renewal, RenewalCreate, RenewalMember} from '@app/features/renewals/models/renewal';
import {BaseApiService} from '@app/shared/services/api-service/base-api.service';
import {PageRequest, PagedData} from '@app/shared/services/api-service/models';


@Injectable()
export class RenewalService extends BaseApiService<RenewalMember, RenewalCreate, Renewal>{

  constructor(http: HttpClient) {
    super('/api/v1/renewals', http);
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<RenewalMember>> {
    return super.getPagedData(pageRequest).pipe(
      map(pd => {
        if (pd && pd._content) {
          pd._content.forEach(r => {
            const date = r.renewal_date as unknown as string;
            r.renewal_date = DateTime.fromISO(date);
          })
        }
        return pd;
      })
    );
  }

  override create(create: RenewalCreate): Observable<Renewal> {
    return super.create(create).pipe(
      map(data => {
        const date = data.renewal_date as unknown as string;
        data.renewal_date = DateTime.fromISO(date);
        return data;
      })
    );
  }

  override update(update: Renewal): Observable<Renewal> {
    return super.update(update).pipe(
      map(data => {
        const date = data.renewal_date as unknown as string;
        data.renewal_date = DateTime.fromISO(date);
        return data;
      })
    );
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }
}
