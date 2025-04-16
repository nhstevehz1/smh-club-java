import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {Address, AddressMember, AddressType} from '@app/features/addresses/models/address';
import {BaseApiService} from '@app/shared/services/api-service/base-api.service';
import {PageRequest, PagedData} from '@app/shared/services/api-service/models';

@Injectable()
export class AddressService extends BaseApiService<Address, AddressMember>{

  constructor(http: HttpClient) {
    super('/api/v1/addresses', http)
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<AddressMember>> {
    return super.getPagedData(pageRequest).pipe(
      map(pd => {
        if (pd && pd._content) {
          pd._content.forEach(a => a.address_type = a.address_type as unknown as AddressType);
        }
        return pd;
      }));
  }

  override create(create: Address): Observable<Address> {
    return super.create(create).pipe(
      map(data => {
        data.address_type = data.address_type as unknown as AddressType;
        return data;
      })
    );
  }

  override update(update: Address): Observable<Address> {
    return super.update(update).pipe(
      map(data => {
        data.address_type = data.address_type as unknown as AddressType;
        return data;
      })
    );
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }
}
