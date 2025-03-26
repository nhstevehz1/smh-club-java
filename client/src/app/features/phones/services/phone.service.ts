import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Phone, PhoneCreate, PhoneMember} from "../models/phone";
import {PagedData} from "../../../shared/models/paged-data";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {PhoneType} from "../models/phone-type";
import {NonNullableFormBuilder, Validators} from "@angular/forms";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {ColumnDef} from '../../../shared/components/sortable-pageable-table/models/column-def';
import {BaseApiService} from '../../../shared/services/base-api-service';

@Injectable()
export class PhoneService extends BaseApiService {
  private  BASE_API = '/api/v1/phones';

  constructor(private http: HttpClient, private fb: NonNullableFormBuilder) {
    super();
  }

  getPhones(pageRequest: PageRequest): Observable<PagedData<PhoneMember>> {
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

  createPhone(create: PhoneCreate): Observable<Phone> {
    return this.http.post<Phone>(this.BASE_API, create).pipe(
      map(data => {
        data.phone_type = data.phone_type as unknown as PhoneType
        return data;
      })
    )
  }

  updatePhone(update: Phone): Observable<Phone> {
    return this.http.put<Phone>(`${this.BASE_API}/${update.id}`, update).pipe(
      map(data => {
        data.phone_type = data.phone_type as unknown as PhoneType
        return data;
      })
    )
  }

  deletePhone(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_API}/${id}`);
  }

  generatePhoneForm(): FormModelGroup<Phone> {
    return this.fb.group({
        id: [0],
        member_id: [0],
        country_code: ['', [Validators.required]],
        phone_number: ['', [Validators.required]],
        phone_type: [PhoneType.Home, [Validators.required]],
    });
  }

  getColumnDefs(): ColumnDef<PhoneMember>[] {
    return [
      {
        columnName: 'phone_number',
        displayName: 'phones.list.columns.phoneNumber',
        isSortable: true,
        cell: (element: PhoneMember) => this.getPhoneNumber(element)
      },
      {
        columnName: 'phone_type',
        displayName: 'phones.list.columns.phoneType',
        isSortable: false,
        cell: (element: PhoneMember) => this.phoneTypeMap.get(element.phone_type)
      },
      {
        columnName: 'full_name',
        displayName: 'phones.list.columns.fullName',
        isSortable: true,
        cell: (element: PhoneMember) => this.getFullName(element.full_name)
      },
    ];
  }

  private getPhoneNumber(phoneMember: PhoneMember): string {
    const code = phoneMember.country_code;
    const number = phoneMember.phone_number;

    // format phone number exp: (555) 555-5555
    const regex = /^(\d{3})(\d{3})(\d{4})$/;
    const formated = number.replace(regex, '($1) $2-$3');

    return `+${code} ${formated}`;
  }

  private phoneTypeMap = new Map<PhoneType, string>([
    [PhoneType.Work, 'phones.type.work'],
    [PhoneType.Home, 'phones.type.home'],
    [PhoneType.Mobile, 'phones.type.mobile']
  ]);
}
