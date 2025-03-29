import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Address, AddressCreate, AddressMember} from '../models';
import {BaseApiService} from '../../../shared/services';
import {PagedData, PageRequest} from '../../../shared/models';
import {AddressType} from '../models/address-type';

@Injectable()
export class AddressService extends BaseApiService<AddressMember, AddressCreate, Address>{

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

  override create(create: AddressCreate): Observable<Address> {
    return super.create(create).pipe(
      map(data => {
        data.address_type = data.address_type as unknown as AddressType;
        return data;
      })
    );
  }

  override update(id: number, update: Address): Observable<Address> {
    return super.update(id, update).pipe(
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
