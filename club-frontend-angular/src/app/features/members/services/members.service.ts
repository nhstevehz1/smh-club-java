import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {PageRequest} from "../../../shared/models/page-request";
import {PagedData} from "../../../shared/models/paged-data";
import {Member, MemberCreate} from "../models/member";
import {map} from "rxjs/operators";
import {DateTime, type DateTimeMaybeValid} from "luxon";

@Injectable()
export class MembersService {
  private  BASE_API = '/api/v1/members';

  constructor(private http: HttpClient) {}

  getMembers(pageRequest: PageRequest): Observable<PagedData<Member>> {
    let query = pageRequest.createQuery();
    let uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this.http.get<PagedData<Member>>(uri).pipe(
      map(data => {
        data._content.forEach(m => {
          //m.birth_date = DateTime.fromISO(m.birth_date as any, {zone: 'utc'});
          //m.joined_date = DateTime.fromISO(m.joined_date as any, {zone: 'utc'})
        });
        return data;
      })
    );
  }

  createMember(memberData: MemberCreate): Observable<Member> {
    return this.http.post<Member>(this.BASE_API, memberData);
  }
}
