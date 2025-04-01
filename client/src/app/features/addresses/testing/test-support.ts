import {FormControl, FormGroup} from '@angular/forms';

import {generatePagedData} from '@app/shared/testing';
import {FormModelGroup} from '@app/shared/components/base-editor';
import {EditAction, EditDialogInput} from '@app/shared/components/edit-dialog';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table';
import {PagedData} from '@app/shared/services/api-service';

import {AddressMember, AddressType, AddressCreate, AddressModels} from '@app/features/addresses/models/address';
import {AddressEditorComponent} from '@app/features/addresses/address-editor/address-editor.component';

export function generateAddressPagedData(page: number, size: number, total: number): PagedData<AddressMember> {
    const content = generateAddressList(size);
    return generatePagedData(page, size, total, content);
}

export function generateAddressList(size: number): AddressMember[] {
  const list: AddressMember[] = [];

  for (let ii = 0; ii < size; ii++) {
    list.push(generateAddressMember(ii));
  }
  return list;
}

export function generateAddressMember(prefix: number): AddressMember {
  return {
    id: prefix,
    member_id: prefix,
    address1: prefix + ' Street',
    address2: prefix + ' Apt.',
    city: prefix + ' City',
    state: prefix + ' State',
    postal_code: prefix + 'Zip',
    address_type: AddressType.Home,
    full_name: {
      first_name: prefix + ' First',
      middle_name: prefix + ' Middle',
      last_name: prefix + ' Last',
      suffix: prefix + ' Suffix'
    }
  }
}

export function generateAddressCreate(): AddressCreate {
  return {
    address1: 'Address1',
    address2: 'Apt.',
    city: 'City',
    state: ' State',
    postal_code: 'Zip',
    address_type: AddressType.Home
  }
}

export function generateAddress(): AddressModels {
  return {
    id: 0,
    member_id: 0,
    address1: 'Address1',
    address2: 'Apt.',
    city: 'City',
    state: ' State',
    postal_code: 'Zip',
    address_type: AddressType.Home
  }
}

export function generateAddressForm(): FormModelGroup<AddressModels> {
  return new FormGroup({
    id: new FormControl(0, {nonNullable: true}),
    member_id: new FormControl(0, {nonNullable: true}),
    address1: new FormControl('', {nonNullable: true}),
    address2: new FormControl('', {nonNullable: true}),
    city: new FormControl('', {nonNullable: true}),
    state: new FormControl('', {nonNullable: true}),
    postal_code: new FormControl('', {nonNullable: true}),
    address_type: new FormControl<AddressType>(AddressType.Home, {nonNullable: true})
  });
}

export function generateAddressDialogInput(context: AddressModels, action: EditAction): EditDialogInput<AddressModels>{
  return {
    title: 'test',
    component: AddressEditorComponent,
    form: generateAddressForm(),
    context: context,
    action: action
  }
}

export function generateAddressColumnDefs(): ColumnDef<AddressMember>[] {
  return [{
      columnName: 'address1',
      displayName: 'addresses.list.columns.address',
      isSortable: true,
      cell: (element: AddressMember) => `${element.address1}`
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
      cell: (element: AddressMember) => `${element.address_type}`
    }, {
      columnName: 'full_name',
      displayName: 'addresses.list.columns.fullName',
      isSortable: true,
      cell: (element: AddressMember) => `${element.full_name}`
    }
  ];
}
