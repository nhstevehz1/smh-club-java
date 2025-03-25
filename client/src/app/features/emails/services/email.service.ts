import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {EmailCreate, EmailMember, Email} from "../models/email";
import {PagedData} from "../../../shared/models/paged-data";
import {map} from "rxjs/operators";
import {EmailType} from "../models/email-type";
import {NonNullableFormBuilder, Validators} from "@angular/forms";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";

@Injectable()
export class EmailService {
  private  BASE_API = '/api/v1/emails';

  constructor(private http: HttpClient,
              private fb: NonNullableFormBuilder) {}

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
}
