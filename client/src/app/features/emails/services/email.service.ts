import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {PagedData, PageRequest} from '@app/shared/services/api-service/models';
import {Email, EmailMember, EmailType} from '@app/features/emails/models/email';
import {BaseApiService} from '@app/shared/services/api-service/base-api.service';
import {FilterByMemberService} from '@app/shared/services/api-service/filter-by-member-service';

@Injectable()
export class EmailService extends BaseApiService<Email, EmailMember> implements FilterByMemberService<Email>{

  constructor(http: HttpClient) {
    super('/api/v1/emails', http);
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<EmailMember>> {
    return super.getPagedData(pageRequest).pipe(
      map(pd => {
        if (pd && pd._content) {
          pd._content.map(e => this.castEmailType(e));
        }
        return pd;
      })
    );
  }

  override create(create: Email): Observable<Email> {
    return super.create(create).pipe(
      map(data => this.castEmailType(data))
    )
  }

  override update(update: Email): Observable<Email> {
    return super.update(update).pipe(
      map(data => this.castEmailType(data))
    )
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }

  public getAllByMember(memberId: number): Observable<Email[]> {
    const uri = `/members/${memberId}/emails`;
    return this.http.get<Email[]>(uri).pipe(
      map(data => data.map(email => this.castEmailType(email)))
    )
  }

  private castEmailType(email: Email): Email {
    email.email_type = email.email_type as unknown as EmailType;
    return email;
  }
}
