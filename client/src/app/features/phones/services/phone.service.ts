import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {PagedData, PageRequest} from '@app/shared/services/api-service/models';
import {Phone, PhoneMember, PhoneType} from '@app/features/phones/models/phone';
import {BaseApiService} from '@app/shared/services/api-service/base-api.service';
import {FilterByMemberService} from '@app/shared/services/api-service/filter-by-member-service';

@Injectable()
export class PhoneService extends BaseApiService<Phone, PhoneMember> implements FilterByMemberService<Phone>{

  constructor(http: HttpClient) {
    super('/api/v1/phones', http);
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<PhoneMember>> {
    return super.getPagedData(pageRequest).pipe(
        map(pd => {
          if (pd && pd._content) {
              pd._content.map(p => this.castPhoneType(p));
          }
          return pd;
        })
    );
  }

  override create(create: Phone): Observable<Phone> {
    return super.create(create).pipe(
      map(data => this.castPhoneType(data))
    )
  }

  override update(update: Phone): Observable<Phone> {
    return super.update(update).pipe(
      map(data => this.castPhoneType(data))
    )
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }

  public getAllByMember(memberId: number): Observable<Phone[]> {
    const uri = `/api/v1/members/${memberId}/phones`;
    return this.http.get<Phone[]>(uri).pipe(
      map(data => data.map(phone => this.castPhoneType(phone)))
    )
  }

  private castPhoneType(phone: Phone): Phone {
    phone.phone_type = phone.phone_type as unknown as PhoneType;
    return phone;
  }
}
