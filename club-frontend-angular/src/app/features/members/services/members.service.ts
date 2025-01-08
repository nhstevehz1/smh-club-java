import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PageRequest} from "../../../shared/models/page-request";
import {PagedData} from "../../../shared/models/PagedData";
import {Member} from "../models/Member";

@Injectable()
export class MembersService {
  private  BASE_API = '/api/v1/members';

  private _http: HttpClient = inject(HttpClient);

  getMembers(pageRequest: PageRequest): Observable<PagedData<Member>> {
    let query = pageRequest.createQuery();
    let uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this._http.get<PagedData<Member>>(uri);
  }

}
