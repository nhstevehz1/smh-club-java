import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {EmailMember} from "../models/email-member";
import {PagedData} from "../../../shared/models/paged-data";

@Injectable()
export class EmailService {
  private  BASE_API = '/api/v1/emails';

  private _http: HttpClient = inject(HttpClient);

  public getEmails(pageRequest: PageRequest): Observable<PagedData<EmailMember>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this._http.get<PagedData<EmailMember>>(uri);
  }
}
