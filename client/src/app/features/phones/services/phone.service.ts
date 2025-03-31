import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {BaseApiService, PagedData, PageRequest} from '@app/shared/services/api-service';

import {Phone, PhoneCreate, PhoneMember, PhoneType} from '@app/features/phones';

@Injectable()
export class PhoneService extends BaseApiService<PhoneMember, PhoneCreate, Phone> {

  constructor(http: HttpClient) {
    super('/api/v1/phones', http);
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<PhoneMember>> {
    return super.getPagedData(pageRequest).pipe(
        map(pd => {
          if (pd && pd._content) {
              pd._content.forEach(p => p.phone_type as unknown as PhoneType);
          }
          return pd;
        })
    );
  }

  override create(create: PhoneCreate): Observable<Phone> {
    return super.create(create).pipe(
      map(data => {
        data.phone_type = data.phone_type as unknown as PhoneType
        return data;
      })
    )
  }

  override update(id: number, update: Phone): Observable<Phone> {
    return super.update(id, update).pipe(
      map(data => {
        data.phone_type = data.phone_type as unknown as PhoneType
        return data;
      })
    )
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }
}
