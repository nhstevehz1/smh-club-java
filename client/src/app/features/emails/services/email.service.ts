import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {Email, EmailCreate, EmailMember} from "../models/email";
import {PagedData} from "../../../shared/models/paged-data";
import {map} from "rxjs/operators";
import {EmailType} from "../models/email-type";
import {NonNullableFormBuilder, Validators} from "@angular/forms";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {BaseApiService} from '../../../shared/services/base-api-service';
import {ColumnDef} from '../../../shared/components/sortable-pageable-table/models/column-def';

@Injectable()
export class EmailService extends BaseApiService {
  private  BASE_API = '/api/v1/emails';

  constructor(private http: HttpClient,
              private fb: NonNullableFormBuilder) {
    super();
  }

  getEmails(pageRequest: PageRequest): Observable<PagedData<EmailMember>> {
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

  createEmail(create: EmailCreate): Observable<Email> {
    return this.http.post<Email>(this.BASE_API, create).pipe(
      map(data => {
        data.email_type = data.email_type as unknown as EmailType;
        return data;
      })
    )
  }

  updateEmail(update: Email): Observable<Email> {
    return this.http.put<Email>(`${this.BASE_API}/${update.id}`, update).pipe(
      map(data => {
        data.email_type = data.email_type as unknown as EmailType;
        return data;
      })
    )
  }

  deleteEmail(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_API}/${id}`);
  }

  generateEmailForm(): FormModelGroup<Email> {
    return this.fb.group({
      id: [0],
      member_id: [0],
      email: ['', [Validators.required, Validators.email]],
      email_type: [EmailType.Home, [Validators.required]]
    });
  }

  generateCreateForm(): FormModelGroup<EmailCreate> {
      return this.fb.group({
          email: ['', [Validators.required, Validators.email]],
          email_type: [EmailType.Home, [Validators.required]]
      });
  }

  generateUpdateForm(update: Email): FormModelGroup<Email> {
      return this.fb.group({
          id: [update.id],
          member_id: [update.member_id],
          email: [update.email, [Validators.required, Validators.email]],
          email_type: [update.email_type, [Validators.required]]
      });
  }

  getColumnDefs(): ColumnDef<EmailMember>[] {
    return [
      {
        columnName: 'email',
        displayName: 'emails.list.columns.email',
        isSortable: true,
        cell:(element: EmailMember) => `${element.email}`
      },
      {
        columnName: 'email_type',
        displayName: 'emails.list.columns.emailType',
        isSortable: false,
        cell:(element: EmailMember) => this.emailTypeMap.get(element.email_type)
      },
      {
        columnName: 'full_name',
        displayName: 'emails.list.columns.fullName',
        isSortable: true,
        cell:(element: EmailMember) => this.getFullName(element.full_name)
      }
    ];
  }

  private emailTypeMap = new Map<EmailType, string>([
    [EmailType.Work, 'emails.type.work'],
    [EmailType.Home, 'emails.type.home'],
    [EmailType.Other, 'emails.type.other']
  ]);
}
