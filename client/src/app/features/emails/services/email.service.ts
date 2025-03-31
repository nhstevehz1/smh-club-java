import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {BaseApiService, PagedData, PageRequest} from '@app/shared/services/api-service';

import {Email, EmailCreate, EmailMember, EmailType} from '@app/features/emails';

@Injectable()
export class EmailService extends BaseApiService<EmailMember, EmailCreate, Email> {

  constructor(http: HttpClient) {
    super('/api/v1/emails', http);
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<EmailMember>> {
    return super.getPagedData(pageRequest).pipe(
      map(pd => {
        if (pd && pd._content) {
          pd._content.forEach(e => e.email_type = e.email_type as unknown as EmailType);
        }
        return pd;
      })
    );
  }

  override create(create: EmailCreate): Observable<Email> {
    return super.create(create).pipe(
      map(data => {
        data.email_type = data.email_type as unknown as EmailType;
        return data;
      })
    )
  }

  override update(id: number, update: Email): Observable<Email> {
    return super.update(id, update).pipe(
      map(data => {
        data.email_type = data.email_type as unknown as EmailType;
        return data;
      })
    )
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }
}
