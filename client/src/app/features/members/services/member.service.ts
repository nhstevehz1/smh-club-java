import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {DateTime} from 'luxon';

import {Member} from '@app/features/members/models/member';
import {BaseApiService} from '@app/shared/services/api-service/base-api.service';
import {PageRequest, PagedData} from '@app/shared/services/api-service/models';
import {toSignal} from '@angular/core/rxjs-interop';

@Injectable()
export class MemberService extends BaseApiService<Member, Member> {

  constructor(http: HttpClient) {
    super('/api/v1/members', http);
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<Member>> {
    return super.getPagedData(pageRequest).pipe(
        map(pd => {
          if(pd && pd._content){
            pd._content.map(m => this.castDateTimeValues(m))
          }
          return pd;
        })
    );
  }

  override get(id: number): Observable<Member> {
    return super.get(id).pipe(
      map(data => this.castDateTimeValues(data))
    );
  }

  override create(create: Member): Observable<Member> {
    return super.create(create).pipe(
      map(data => this.castDateTimeValues(data))
    );
  }

  override update(update: Member): Observable<Member> {
    return super.update(update).pipe(
      map(data => this.castDateTimeValues(data))
    )
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }

  private castDateTimeValues(member: Member): Member {
      let date = member.birth_date as unknown as string;
      member.birth_date = DateTime.fromISO(date);

      date = member.joined_date as unknown as string;
      member.joined_date = DateTime.fromISO(date);

      return member;
  }
}
