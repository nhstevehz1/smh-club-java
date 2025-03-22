import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {PagedData} from "../../../shared/models/paged-data";
import {Address, AddressDetails, AddressMember} from "../models/address";
import {map} from "rxjs/operators";
import {AddressType} from "../models/address-type";
import {NonNullableFormBuilder, Validators} from "@angular/forms";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";

@Injectable()
export class AddressService {
  public readonly BASE_API = '/api/v1/addresses';

  constructor(private http: HttpClient, private fb: NonNullableFormBuilder) {}

  getAddresses(pageRequest: PageRequest): Observable<PagedData<AddressMember>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this.http.get<PagedData<AddressMember>>(uri).pipe(
      map(pd => {
        if (pd && pd._content) {
          pd._content.forEach(a => a.address_type = a.address_type as unknown as AddressType);
        }
        return pd;
      })
    );
  }

  createAddress(create: Address): Observable<AddressDetails> {
    return this.http.post<Address>(this.BASE_API, create).pipe(
        map(data => JSON.stringify(data)),
        map(data => JSON.parse(data) as AddressDetails),
        map(data => {
          data.address_type = data.address_type as unknown as AddressType;
          return data;
        })
    );
  }

  updateAddress(update: Address): Observable<AddressDetails> {
    return this.http.put<AddressDetails>(`${this.BASE_API}/`, update).pipe(
        map(data => JSON.stringify(data)),
        map(data => JSON.parse(data) as AddressDetails),
        map(data => {
          data.address_type = data.address_type as unknown as AddressType;
          return data;
        })
    );
  }

  generateCreateForm(): FormModelGroup<Address> {
    return this.fb.group({
      address1: ['', [Validators.required]],
      address2: [''],
      city: ['', [Validators.required]],
      state: ['', [Validators.required, Validators.minLength(2)]],
      postal_code: ['', [Validators.required, Validators.minLength(5)]],
      address_type: [AddressType.Home, [Validators.required]]
    });
  }

  generateUpdateForm(update: AddressDetails): FormModelGroup<AddressDetails> {
    return this.fb.group({
      id: [update.id, Validators.required],
      member_id: [update.member_id, [Validators.required, Validators.min(1)]],
      address1: [update.address1, [Validators.required]],
      address2: [update.address2],
      city: [update.city, [Validators.required]],
      state: [update.state, [Validators.required, Validators.minLength(2)]],
      postal_code: [update.postal_code, [Validators.required, Validators.minLength(5)]],
      address_type: [update.address_type, [Validators.required]]
    });
  }

}
