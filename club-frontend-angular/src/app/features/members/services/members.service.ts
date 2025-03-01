import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PageRequest} from "../../../shared/models/page-request";
import {PagedData} from "../../../shared/models/paged-data";
import {Member, MemberCreate} from "../models/member";
import {map} from "rxjs/operators";
import {DateTime} from "luxon";

@Injectable()
export class MembersService {
  private  BASE_API = '/api/v1/members';

  constructor(private http: HttpClient) {}

  getMembers(pageRequest: PageRequest): Observable<PagedData<Member>> {
    let query = pageRequest.createQuery();
    let uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this.http.get<PagedData<Member>>(uri).pipe(
        map(pd => {
          pd._content.forEach(m => {
            let date = m.birth_date as unknown as string;
            m.birth_date = DateTime.fromISO(date);

            date = m.joined_date as unknown as string;
            m.joined_date = DateTime.fromISO(date);
          })
          return pd;
        })
    );
  }

  createMember(memberData: MemberCreate): Observable<Member> {
    return this.http.post<Member>(this.BASE_API, memberData);
  }
}
