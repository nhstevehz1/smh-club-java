import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {Address, AddressMember, AddressType} from '@app/features/addresses/models/address';
import {BaseApiService} from '@app/shared/services/api-service/base-api.service';
import {PageRequest, PagedData} from '@app/shared/services/api-service/models';
import {FilterByMemberService} from '@app/shared/services/api-service/filter-by-member-service';

@Injectable()
export class AddressService extends BaseApiService<Address, AddressMember> implements FilterByMemberService<Address>{

  constructor(http: HttpClient) {
    super('/api/v1/addresses', http)
  }

  override getPagedData(pageRequest: PageRequest): Observable<PagedData<AddressMember>> {
    return super.getPagedData(pageRequest).pipe(
      map(pd => {
        if (pd && pd._content) {
          pd._content.map(address => this.castAddressType(address));
        }
        return pd;
      }));
  }

  override create(create: Address): Observable<Address> {
    return super.create(create).pipe(
      map(data => this.castAddressType(data))
    );
  }

  override update(update: Address): Observable<Address> {
    return super.update(update).pipe(
      map(data => this.castAddressType(data))
    );
  }

  override delete(id: number): Observable<void> {
    return super.delete(id);
  }

  public getAllByMember(memberId: number): Observable<Address[]> {
    const uri = `/members/${memberId}/addresses`;
    return this.http.get<Address[]>(uri).pipe(
      map(data => data.map(address => this.castAddressType(address)))
    );
  }

  private castAddressType(address: Address): Address {
    address.address_type = address.address_type as unknown as AddressType;
    return address;
  }
}
