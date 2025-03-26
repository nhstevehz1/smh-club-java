import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {PagedData} from "../../../shared/models/paged-data";
import {Renewal, RenewalCreate, RenewalMember} from "../models/renewal";
import {map} from 'rxjs/operators';
import {DateTime} from 'luxon';
import {FormModelGroup} from '../../../shared/components/base-editor/form-model-group';
import {NonNullableFormBuilder, Validators} from '@angular/forms';
import {ColumnDef} from '../../../shared/components/sortable-pageable-table/models/column-def';
import {BaseApiService} from '../../../shared/services/base-api-service';

@Injectable()
export class RenewalService extends BaseApiService{
  private  BASE_API = '/api/v1/renewals';

  constructor(private http: HttpClient, private fb: NonNullableFormBuilder) {
    super();
  }

  public getRenewals(pageRequest: PageRequest): Observable<PagedData<RenewalMember>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this.http.get<PagedData<RenewalMember>>(uri).pipe(
      map(pd => {
        if (pd && pd._content) {
          pd._content.forEach(r => {
            const date = r.renewal_date as unknown as string;
            r.renewal_date = DateTime.fromISO(date);
          })
        }
        return pd;
      })
    );
  }

  createRenewal(create: RenewalCreate): Observable<Renewal> {
    return this.http.post<Renewal>(this.BASE_API, create).pipe(
      map(data => {
        const date = data.renewal_date as unknown as string;
        data.renewal_date = DateTime.fromISO(date);
        return data;
      })
    );
  }

  updateRenewal(update: Renewal): Observable<Renewal> {
    return this.http.post<Renewal>(`${this.BASE_API}/${update.id}`, update).pipe(
      map(data => {
        const date = data.renewal_date as unknown as string;
        data.renewal_date = DateTime.fromISO(date);
        return data;
      })
    );
  }

  deleteRenewal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_API}/${id}`);
  }

  generateRenewalForm(): FormModelGroup<Renewal> {
    return this.fb.group({
      id: [0],
      member_id: [0],
      renewal_date: [DateTime.now(), [Validators.required]],
      renewal_year: [DateTime.now().year, [Validators.required]]
    });
  }

  getColumnDefs(): ColumnDef<RenewalMember>[] {
    return [
      {
        columnName: 'renewal_date',
        displayName: 'renewals.list.columns.renewalDate',
        isSortable: true,
        cell: (element: RenewalMember) => `${element.renewal_date}`,
      },
      {
        columnName: 'renewal_year',
        displayName: 'renewals.list.columns.renewalYear',
        isSortable: true,
        cell: (element: RenewalMember) => `${element.renewal_year}`
      },
      {
        columnName: 'full_name',
        displayName: 'renewals.list.columns.fullName',
        isSortable: true,
        cell: (element: RenewalMember) => this.getFullName(element.full_name)
      }
    ];
  }
}
