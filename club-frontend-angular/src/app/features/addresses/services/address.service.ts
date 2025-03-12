import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {PagedData} from "../../../shared/models/paged-data";
import {AddressCreate, AddressDetails, AddressMember, AddressUpdate} from "../models/address";
import {map} from "rxjs/operators";
import {AddressType} from "../models/address-type";
import {FormGroup, NonNullableFormBuilder, Validators} from "@angular/forms";

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

  createAddress(create: AddressCreate): Observable<AddressDetails> {

    return this.http.post<AddressCreate>(this.BASE_API, create).pipe(
        map(data => JSON.stringify(data)),
        map(data => JSON.parse(data) as AddressDetails),
        map(data => {
          data.address_type = data.address_type as unknown as AddressType;
          return data;
        })
    );
  }

  updateAddress(update: AddressUpdate): Observable<AddressDetails> {

    return this.http.put<AddressCreate>(`${this.BASE_API}/`, update).pipe(
        map(data => JSON.stringify(data)),
        map(data => JSON.parse(data) as AddressDetails),
        map(data => {
          data.address_type = data.address_type as unknown as AddressType;
          return data;
        })
    );
  }

  generateCreateForm(): FormGroup {
    return this.fb.group({
      address1: ['', Validators.required],
      address2: [''],
      city: ['', Validators.required],
      state: ['', Validators.required],
      postal_code: ['', Validators.required],
      address_type: [AddressType.Home, Validators.required]
    });
  }

  generateEditForm(): FormGroup {
    return this.fb.group({
      id: [0, Validators.required],
      member_number: [0, [Validators.required, Validators.min(1)]],
      address1: ['', Validators.required],
      address2: [''],
      city: ['', Validators.required],
      state: ['', Validators.required],
      postal_code: ['', Validators.required],
      address_type: [AddressType.Home, Validators.required]
    });
  }

}
