import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {EmailMember} from "../models/email";
import {PagedData} from "../../../shared/models/paged-data";
import {map} from "rxjs/operators";
import {EmailType} from "../models/email-type";

@Injectable()
export class EmailService {
  private  BASE_API = '/api/v1/emails';

  constructor(private http: HttpClient) {}

  public getEmails(pageRequest: PageRequest): Observable<PagedData<EmailMember>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this.http.get<PagedData<EmailMember>>(uri).pipe(
      map(pd => {
        if (pd && pd._content) {
          pd._content.forEach(e => e.email_type = e.email_type as unknown as EmailType);
        }
        return pd;
      })
    );
  }
}
