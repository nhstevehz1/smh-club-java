import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {DateTime} from "luxon";

import {BaseApiService, PagedData, PageRequest} from '@app/shared/services';
import {Member, MemberCreate} from '@app/features/members';

@Injectable()
export class MemberService extends BaseApiService<Member, MemberCreate, Member> {

  constructor(http: HttpClient) {
    super('/api/v1/members', http);
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<Member>> {
    return super.getPagedData(pageRequest).pipe(
        map(pd => {
          if(pd && pd._content){
              pd._content.forEach(m => {
                let date = m.birth_date as unknown as string;
                m.birth_date = DateTime.fromISO(date);

                date = m.joined_date as unknown as string;
                m.joined_date = DateTime.fromISO(date);
              })
          }
          return pd;
        })
    );
  }

  override create(create: MemberCreate): Observable<Member> {
    return super.create(create).pipe(
      map(data => {
        let date = data.birth_date as unknown as string;
        data.birth_date = DateTime.fromISO(date);

        date = data.joined_date as unknown as string;
        data.joined_date = DateTime.fromISO(date);
        return data;
      })
    );
  }

  override update(id: number, update: Member): Observable<Member> {
    return super.update(id, update).pipe(
      map(data => {
        let date = data.birth_date as unknown as string;
        data.birth_date = DateTime.fromISO(date);

        date = data.joined_date as unknown as string;
        data.joined_date = DateTime.fromISO(date);
        return data;
      })
    )
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }

}
