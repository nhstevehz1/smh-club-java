import { Injectable } from '@angular/core';

import {ColumnDef} from '@app/shared/components/sortable-pageable-table';
import {BaseTableService} from '@app/shared/services/table-service';

import {AddressMember, AddressType} from '@app/features/addresses';

@Injectable()
export class AddressTableService extends BaseTableService<AddressMember> {
  constructor() {
    super();
  }

  getColumnDefs(): ColumnDef<AddressMember>[] {
    return [{
        columnName: 'address1',
        displayName: 'addresses.list.columns.address',
        isSortable: true,
        cell: (element: AddressMember) => this.getStreet(element)
      }, {
        columnName: 'city',
        displayName: 'addresses.list.columns.city',
        isSortable: true,
        cell: (element: AddressMember) => `${element.city}`
      }, {
        columnName: 'state',
        displayName: 'addresses.list.columns.state',
        isSortable: true,
        cell: (element: AddressMember) => `${element.state}`
      }, {
        columnName: 'zip',
        displayName: 'addresses.list.columns.postalCode',
        isSortable: true,
        cell: (element: AddressMember) => `${element.postal_code}`
      }, {
        columnName: 'address_type',
        displayName: 'addresses.list.columns.addressType',
        isSortable: false,
        cell: (element: AddressMember) => this.addressTypeMap.get(element.address_type)//`${element.address_type}`
      }, {
        columnName: 'full_name',
        displayName: 'addresses.list.columns.fullName',
        isSortable: true,
        cell: (element: AddressMember) =>  this.getFullName(element.full_name) //`${element.full_name.last_first}`
      }];
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
