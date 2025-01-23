import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {PhoneMember} from "../models/phone-member";
import {PagedData} from "../../../shared/models/paged-data";
import {Observable} from "rxjs";

@Injectable()
export class PhoneService {
  private  BASE_API = '/api/v1/phones';

  constructor(private http: HttpClient) {}

  public getPhones(pageRequest: PageRequest): Observable<PagedData<PhoneMember>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this.http.get<PagedData<PhoneMember>>(uri);
  }
}
