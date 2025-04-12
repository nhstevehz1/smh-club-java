import {FormControl, FormGroup} from '@angular/forms';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {EditAction, EditDialogInput} from '@app/shared/components/base-edit-dialog/models';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';
import {AddressMember, AddressType, AddressCreate, Address} from '@app/features/addresses/models/address';
import {AddressEditorComponent} from '@app/features/addresses/address-editor/address-editor.component';
import {PagedData} from '@app/shared/services/api-service/models';
import {TestHelpers} from '@app/shared/testing';

export class AddressTest {
  static generatePagedData(page: number, size: number, total: number): PagedData<AddressMember> {
    const content = this.generateList(size);
    return TestHelpers.generatePagedData(page, size, total, content);
  }

  static generateList(size: number): AddressMember[] {
    const list: AddressMember[] = [];

    for (let ii = 0; ii < size; ii++) {
      list.push(this.generateAddressMember(ii));
    }
    return list;
  }

  static generateAddressMember(prefix: number): AddressMember {
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

  static generateAddress(): Address {
    const prefix = 2;
    return {
      id: prefix,
      member_id: prefix,
      address1: prefix + 'Address1',
      address2: 'Apt. ' + prefix,
      city: prefix + 'City',
      state: prefix + 'State',
      postal_code: '55555',
      address_type: AddressType.Home
    }
  }

  static generateForm(): FormModelGroup<Address> {
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

  static generateDialogInput(context: Address, action: EditAction): EditDialogInput<Address, AddressEditorComponent>{
    return {
      title: 'test',
      context: context,
      action: action,
      editorConfig: {
        component: AddressEditorComponent,
        form: this.generateForm()
      }
    }
  }

  static generateColumnDefs(): ColumnDef<AddressMember>[] {
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
}
