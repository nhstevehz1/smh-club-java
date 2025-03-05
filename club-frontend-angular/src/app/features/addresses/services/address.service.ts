import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {PagedData} from "../../../shared/models/paged-data";
import {AddressMember} from "../models/address-member";
import {map} from "rxjs/operators";
import {AddressType} from "../models/address-type";

@Injectable()
export class AddressService {
  private  BASE_API = '/api/v1/addresses';

  constructor(private http: HttpClient) {}

  getAddresses(pageRequest: PageRequest): Observable<PagedData<AddressMember>> {
    const query = pageRequest.createQuery();
    const uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this.http.get<PagedData<AddressMember>>(uri).pipe(
      map(pd => {
        pd._content.forEach(a => a.address_type = a.address_type as unknown as AddressType);
        return pd;
      })
    );
  }

}
