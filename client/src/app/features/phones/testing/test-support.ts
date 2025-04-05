import {FormControl, FormGroup} from '@angular/forms';

import {EditAction, EditDialogInput} from '@app/shared/components/base-edit-dialog/models';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';
import {PhoneCreate, PhoneMember, Phone, PhoneType} from '@app/features/phones/models/phone';
import {PhoneEditorComponent} from '@app/features/phones/phone-editor/phone-editor.component';
import {PagedData} from '@app/shared/services/api-service/models';
import {TestHelpers} from '@app/shared/testing';

export class PhoneTest {
  static generatePageData(page: number, size: number, total: number): PagedData<PhoneMember> {
    const content = this.generateList(size);
    return TestHelpers.generatePagedData(page, size, total, content);
  }

  static generateList(size: number): PhoneMember[] {
    const list: PhoneMember[] = [];

    for(let ii = 0; ii < size; ii++) {
      list.push(this.generatePhoneMember(ii));
    }

    return list;
  }

  static generatePhoneMember(prefix: number): PhoneMember {
    return {
      id: prefix,
      member_id: prefix,
      country_code: '1',
      phone_number: `60388399${prefix}`,
      phone_type: PhoneType.Home,
      full_name: {
        first_name: prefix + ' First',
        middle_name: prefix + ' Middle',
        last_name: prefix + ' Last',
        suffix: prefix + ' Suffix'
      }
    }
  }

  static generatePhoneCreate(): PhoneCreate {
    return {
      country_code: '1',
      phone_number: `6035555555`,
      phone_type: PhoneType.Home
    }
  }

  static generatePhone(): Phone {
    return {
      id: 0,
      member_id: 0,
      country_code: '1',
      phone_number: `6035555555`,
      phone_type: PhoneType.Home
    }
  }

  static generateForm(): FormModelGroup<Phone> {
    return new FormGroup({
      id: new FormControl(0, {nonNullable: true}),
      member_id: new FormControl(0, {nonNullable: true}),
      country_code: new FormControl('', {nonNullable: true}),
      phone_number: new FormControl('', {nonNullable: true}),
      phone_type: new FormControl<PhoneType>(PhoneType.Mobile, {nonNullable: true})
    });
  }

  static generateDialogInput(context: Phone, action: EditAction)
    : EditDialogInput<Phone, PhoneEditorComponent> {

    return {
      title: 'title',
      context: context,
      action: action,
      editorConfig: {
        component: PhoneEditorComponent,
        form: this.generateForm()
      }
    }
  }

  static generatePhoneColumnDefs(): ColumnDef<PhoneMember>[] {
    return [{
      columnName: 'phone_number',
      displayName: 'phones.list.columns.phoneNumber',
      isSortable: true,
      cell: (element: PhoneMember) => `${element.phone_number}`
    }, {
      columnName: 'phone_type',
      displayName: 'phones.list.columns.phoneType',
      isSortable: false,
      cell: (element: PhoneMember) => `${element.phone_type}`
    }, {
      columnName: 'full_name',
      displayName: 'phones.list.columns.fullName',
      isSortable: true,
      cell: (element: PhoneMember) => `${element.full_name}`
    }];
  }
}
