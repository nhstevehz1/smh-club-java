import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {PhoneCreate, PhoneMember, PhoneUpdate} from "../models/phone";
import {PagedData} from "../../../shared/models/paged-data";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {PhoneType} from "../models/phone-type";
import {NonNullableFormBuilder, Validators} from "@angular/forms";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";

@Injectable()
export class PhoneService {
  private  BASE_API = '/api/v1/phones';

  constructor(private http: HttpClient,
              private fb: NonNullableFormBuilder) {}

  public getPhones(pageRequest: PageRequest): Observable<PagedData<PhoneMember>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this.http.get<PagedData<PhoneMember>>(uri).pipe(
        map(pd => {
          if (pd && pd._content) {
              pd._content.forEach(p => p.phone_type as unknown as PhoneType);
          }
          return pd;
        })
    );
  }

  generateCreateForm(): FormModelGroup<PhoneCreate> {
      return this.fb.group({
          country_code: ['1', [Validators.required]],
          phone_number: ['', [Validators.required]],
          phone_type: [PhoneType.Home, [Validators.required]],
      });
  }

    generateUpdateForm(update: PhoneUpdate): FormModelGroup<PhoneUpdate> {
        return this.fb.group({
            id: [update.id],
            member_id: [update.member_id],
            country_code: [update.country_code, [Validators.required]],
            phone_number: [update.phone_number, [Validators.required]],
            phone_type: [update.phone_type, [Validators.required]],
        });
    }
}
