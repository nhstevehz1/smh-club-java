import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {PagedData} from "../../../shared/models/paged-data";
import {AddressMember} from "../models/address-member";

@Injectable()
export class AddressService {
  private  BASE_API = '/api/v1/addresses';

  private _http: HttpClient = inject(HttpClient);

  getAddresses(pageRequest: PageRequest): Observable<PagedData<AddressMember>> {
    let query = pageRequest.createQuery();
    let uri = query == null ? this.BASE_API : this.BASE_API + query;

    return this._http.get<PagedData<AddressMember>>(uri);
  }

}
