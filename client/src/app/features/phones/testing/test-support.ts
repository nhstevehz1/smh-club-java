import {PagedData} from "../../../shared/models";
import {generatePagedData} from "@app/shared/testing/test-helpers";
import {PhoneCreate, PhoneMember, Phone, PhoneType} from "../models";
import {FormModelGroup} from "../../../shared/components/base-editor/form-model-group";
import {FormControl, FormGroup} from "@angular/forms";
import {EditAction, EditDialogInput} from '../../../shared/components/edit-dialog/models';
import {PhoneEditorComponent} from '../phone-editor/phone-editor.component';
import {ColumnDef} from '../../../shared/components/sortable-pageable-table/models';

export function generatePhonePageData(page: number, size: number, total: number): PagedData<PhoneMember> {
    const content = generatePhoneList(size);
    return generatePagedData(page, size, total, content);
}

export function generatePhoneList(size: number): PhoneMember[] {
  const list: PhoneMember[] = [];

  for(let ii = 0; ii < size; ii++) {
    list.push(generatePhoneMember(ii));
  }

  return list;
}

export function generatePhoneMember(prefix: number): PhoneMember {
  return {
    id: prefix,
    member_id: prefix,
    country_code: '1',
    phone_number: `60388399${prefix}`,
    phone_type: PhoneType.Home,
    full_name: {
      first_name: prefix + " First",
      middle_name: prefix + " Middle",
      last_name: prefix + " Last",
      suffix: prefix + " Suffix"
    }
  }
}

export function generatePhoneCreate(): PhoneCreate {
  return {
    country_code: '1',
    phone_number: `6035555555`,
    phone_type: PhoneType.Home
  }
}

export function generatePhone(): Phone {
  return {
    id: 0,
    member_id: 0,
    country_code: '1',
    phone_number: `6035555555`,
    phone_type: PhoneType.Home
  }
}

export function generatePhoneForm(): FormModelGroup<Phone> {
  return new FormGroup({
    id: new FormControl(0, {nonNullable: true}),
    member_id: new FormControl(0, {nonNullable: true}),
    country_code: new FormControl('', {nonNullable: true}),
    phone_number: new FormControl('', {nonNullable: true}),
    phone_type: new FormControl<PhoneType>(PhoneType.Mobile, {nonNullable: true})
  });
}

export function generatePhoneDialogInput(context: Phone, action: EditAction): EditDialogInput<Phone> {
  return {
    title: 'title',
    component: PhoneEditorComponent,
    form: generatePhoneForm(),
    context: context,
    action: action
  }
}

export function generatePhoneColumnDefs(): ColumnDef<PhoneMember>[] {
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
