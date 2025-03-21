import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {PagedData} from "../../../shared/models/paged-data";
import {RenewalMember} from "../models/renewal";

@Injectable()
export class RenewalService {
  private  BASE_API = '/api/v1/renewals';

  constructor(private http: HttpClient) {}

  public getRenewals(pageRequest: PageRequest): Observable<PagedData<RenewalMember>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this.http.get<PagedData<RenewalMember>>(uri);
  }
}
