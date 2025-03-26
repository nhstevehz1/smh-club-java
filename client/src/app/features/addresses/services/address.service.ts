import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../../shared/models/page-request";
import {Observable} from "rxjs";
import {PagedData} from "../../../shared/models/paged-data";
import {Address, AddressCreate, AddressMember} from "../models/address";
import {map} from "rxjs/operators";
import {AddressType} from "../models/address-type";
import {NonNullableFormBuilder, Validators} from "@angular/forms";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {BaseApiService} from '../../../shared/services/base-api-service';
import {ColumnDef} from '../../../shared/components/sortable-pageable-table/models/column-def';

@Injectable()
export class AddressService extends BaseApiService {
  public readonly BASE_API = '/api/v1/addresses';

  constructor(private http: HttpClient, private fb: NonNullableFormBuilder) {
    super();
  }

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
    return this.http.post<Address>(this.BASE_API, create).pipe(
        map(data => {
          data.address_type = data.address_type as unknown as AddressType;
          return data;
        })
    );
  }

  updateAddress(update: Address): Observable<Address> {
    return this.http.put<Address>(`${this.BASE_API}/${update.id}`, update).pipe(
        map(data => {
          data.address_type = data.address_type as unknown as AddressType;
          return data;
        })
    );
  }

  deleteAddress(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_API}/${id}`);
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

  getColumnDefs(): ColumnDef<AddressMember>[] {
    return [
      {
        columnName: 'address1',
        displayName: 'addresses.list.columns.address',
        isSortable: true,
        cell: (element: AddressMember) => this.getStreet(element)
      },
      {
        columnName: 'city',
        displayName: 'addresses.list.columns.city',
        isSortable: true,
        cell: (element: AddressMember) => `${element.city}`
      },
      {
        columnName: 'state',
        displayName: 'addresses.list.columns.state',
        isSortable: true,
        cell: (element: AddressMember) => `${element.state}`
      },
      {
        columnName: 'zip',
        displayName: 'addresses.list.columns.postalCode',
        isSortable: true,
        cell: (element: AddressMember) => `${element.postal_code}`
      },
      {
        columnName: 'address_type',
        displayName: 'addresses.list.columns.addressType',
        isSortable: false,
        cell: (element: AddressMember) => this.addressTypeMap.get(element.address_type)//`${element.address_type}`
      },
      {
        columnName: 'full_name',
        displayName: 'addresses.list.columns.fullName',
        isSortable: true,
        cell: (element: AddressMember) =>  this.getFullName(element.full_name) //`${element.full_name.last_first}`
      }
    ];
  }

  private getStreet(address: AddressMember): string {
    const street1 = address.address1;
    const street2 = address.address2 || ''
    return street2.length > 0 ? `${street1}, ${street2}` : street1;
  }

  private readonly addressTypeMap: Map<AddressType, string> = new Map<AddressType, string>([
    [AddressType.Home, 'addresses.type.home'],
    [AddressType.Work, 'addresses.type.work'],
    [AddressType.Other, 'addresses.type.other']
  ]);
}
