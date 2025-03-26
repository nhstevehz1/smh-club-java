import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PageRequest} from "../../../shared/models/page-request";
import {PagedData} from "../../../shared/models/paged-data";
import {Member, MemberCreate} from "../models/member";
import {map} from "rxjs/operators";
import {DateTime} from "luxon";
import {NonNullableFormBuilder, Validators} from "@angular/forms";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {ColumnDef} from '../../../shared/components/sortable-pageable-table/models/column-def';
import {TranslateService} from '@ngx-translate/core';
import {DateTimeToFormatPipe} from '../../../shared/pipes/luxon/date-time-to-format.pipe';

@Injectable()
export class MembersService {
  private  BASE_API = '/api/v1/members';

  constructor(private http: HttpClient, private fb: NonNullableFormBuilder,
              private translate: TranslateService, private dtFormat: DateTimeToFormatPipe) {}

  getMembers(pageRequest: PageRequest): Observable<PagedData<Member>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this.http.get<PagedData<Member>>(uri).pipe(
        map(pd => {
          if(pd && pd._content){
              pd._content.forEach(m => {
                let date = m.birth_date as unknown as string;
                m.birth_date = DateTime.fromISO(date);

                date = m.joined_date as unknown as string;
                m.joined_date = DateTime.fromISO(date);
              })
          }
          return pd;
        })
    );
  }

  createMember(create: MemberCreate): Observable<Member> {
    return this.http.post<Member>(this.BASE_API, create).pipe(
      map(data => {
        let date = data.birth_date as unknown as string;
        data.birth_date = DateTime.fromISO(date);

        date = data.joined_date as unknown as string;
        data.joined_date = DateTime.fromISO(date);
        return data;
      })
    );
  }

  updateMember(update: Member): Observable<Member> {
    return this.http.put<Member>(`${this.BASE_API}/${update.id}`, update).pipe(
      map(data => {
        let date = data.birth_date as unknown as string;
        data.birth_date = DateTime.fromISO(date);

        date = data.joined_date as unknown as string;
        data.joined_date = DateTime.fromISO(date);
        return data;
      })
    )
  }

  deleteMember(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_API}/${id}`);
  }

  generateMemberForm(): FormModelGroup<Member> {
    return this.fb.group({
      id: [0],
      member_number: [0, [Validators.required, Validators.min(1)]],
      first_name: ['', [Validators.required]],
      middle_name: [''],
      last_name: ['', [Validators.required]],
      suffix: [''],
      birth_date: [DateTime.now(), [Validators.required]],
      joined_date: [DateTime.now(), [Validators.required]],
    });
  }

  getColumnDefs(): ColumnDef<Member>[] {
    return [
      {
        columnName: 'member_number',
        displayName: 'members.list.columns.memberNumber',
        translateDisplayName: false,
        isSortable: true,
        cell: (element: Member) => `${element.member_number}`},
      {
        columnName: 'first_name',
        displayName: 'members.list.columns.firstName',
        isSortable: true,
        cell: (element: Member) => this.contactStrings(element.first_name, element.middle_name)
      },
      {
        columnName: 'last_name',
        displayName: 'members.list.columns.lastName',
        isSortable: true,
        cell: (element: Member) => this.contactStrings(element.last_name, element.suffix),
      },
      {
        columnName: 'birth_date',
        displayName: 'members.list.columns.birthDate',
        isSortable: true,
        cell: (element: Member) => {
          return this.dtFormat.transform(element.birth_date, DateTime.DATE_SHORT,
            {locale: this.translate.currentLang});
        }
      },
      {
        columnName: 'joined_date',
        displayName: 'members.list.columns.joinedDate',
        isSortable: true,
        cell: (element: Member) => {
          return this.dtFormat.transform(element.joined_date, DateTime.DATE_SHORT,
            {locale: this.translate.currentLang});
        }
      }
    ];
  }

  private contactStrings(str1: string, str2?: string, delimiter?: string): string {
    const val2 = str2 || '';
    const delim = delimiter || ' ';
    return str1.concat(val2, delim).trim();
  }
}
