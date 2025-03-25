import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {PagedData} from "../../../shared/models/paged-data";
import {AddressCreate, Address, AddressMember} from "../models/address";
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

  createAddress(create: AddressCreate): Observable<Address> {
    return this.http.post<AddressCreate>(this.BASE_API, create).pipe(
        map(data => JSON.stringify(data)),
        map(data => JSON.parse(data) as Address),
        map(data => {
          data.address_type = data.address_type as unknown as AddressType;
          return data;
        })
    );
  }

  updateAddress(update: Address): Observable<Address> {
    return this.http.put<Address>(`${this.BASE_API}/${update.id}`, update).pipe(
        map(data => JSON.stringify(data)),
        map(data => JSON.parse(data) as Address),
        map(data => {
          data.address_type = data.address_type as unknown as AddressType;
          return data;
        })
    );
  }

  deleteAddress(id: number): Observable<number> {
    return this.http.delete<number>(`${this.BASE_API}/${id}`);
  }

  generateAddressForm(): FormModelGroup<Address> {
    return this.fb.group({
      id: [0],
      member_id: [0],
      address1: ['', [Validators.required]],
      address2: [''],
      city: ['', [Validators.required]],
      state: ['', [Validators.required, Validators.minLength(2)]],
      postal_code: ['', [Validators.required, Validators.minLength(5)]],
      address_type: [AddressType.Home, [Validators.required]]
    });
  }

}
