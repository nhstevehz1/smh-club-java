import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PageRequest} from "../../../shared/models/page-request";
import {PagedData} from "../../../shared/models/paged-data";
import {Member, MemberCreate, MemberDetails, MemberUpdate} from "../models/member";
import {map} from "rxjs/operators";
import {DateTime} from "luxon";
import {NonNullableFormBuilder, Validators} from "@angular/forms";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {Address} from "../../addresses/models/address";
import {EmailCreate} from "../../emails/models/email";
import {PhoneCreate} from "../../phones/models/phone";

@Injectable()
export class MembersService {
  private  BASE_API = '/api/v1/members';

  constructor(private http: HttpClient,
              private fb: NonNullableFormBuilder) {}

  getMembers(pageRequest: PageRequest): Observable<PagedData<MemberDetails>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this.http.get<PagedData<MemberDetails>>(uri).pipe(
        map(pd => {
          if(pd && pd._content){
              pd._content.forEach(m => {
                  let date = m.birth_date as unknown as string;
                  m.birth_date = DateTime.fromISO(date).toLocal();

                  date = m.joined_date as unknown as string;
                  m.joined_date = DateTime.fromISO(date).toLocal();
              })
          }
          return pd;
        })
    );
  }

  createMember(memberData: MemberCreate): Observable<Member> {
    return this.http.post<Member>(this.BASE_API, memberData);
  }

  generateCreateForm(addressForm: FormModelGroup<Address>,
                     emailForm: FormModelGroup<EmailCreate>,
                     phoneForm: FormModelGroup<PhoneCreate>): FormModelGroup<MemberCreate> {

      return this.fb.group({
          member_number: [0, [Validators.required, Validators.min(1)]],
          first_name: ['', [Validators.required]],
          middle_name: [''],
          last_name: ['', [Validators.required]],
          suffix: [''],
          birth_date: [DateTime.now, [Validators.required]],
          joined_date: [DateTime.now, [Validators.required]],
          addresses: this.fb.array<FormModelGroup<Address>>([addressForm]),
          emails: this.fb.array<FormModelGroup<EmailCreate>>([emailForm]),
          phones: this.fb.array<FormModelGroup<PhoneCreate>>([phoneForm]),
      }) as unknown as FormModelGroup<MemberCreate>;
  }

  generateUpdateForm(update: MemberUpdate): FormModelGroup<MemberUpdate> {
      return this.fb.group({
          id: [update.id, [Validators.required]],
          member_number: [update.member_number, [Validators.required, Validators.min(1)]],
          first_name: [update.first_name, [Validators.required]],
          middle_name: [update.middle_name],
          last_name: [update.last_name, [Validators.required]],
          suffix: [update.suffix],
          birth_date: [update.birth_date, [Validators.required]],
          joined_date: [update.joined_date, [Validators.required]]
      });
  }
}
