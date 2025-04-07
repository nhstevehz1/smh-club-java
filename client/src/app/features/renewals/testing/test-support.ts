import {DateTime} from 'luxon';

import {RenewalMember, Renewal} from '@app/features/renewals/models/renewal';
import {PagedData} from '@app/shared/services/api-service/models';
import {TestHelpers} from '@app/shared/testing';
import {FormModelGroup} from '@app/shared/components/base-editor/models';
import {FormGroup, FormControl} from '@angular/forms';
import {EditAction, EditDialogInput} from '@app/shared/components/base-edit-dialog/models';
import {RenewalEditorComponent} from '@app/features/renewals/renewal-editor/renewal-editor.component';
import {ColumnDef} from '@app/shared/components/sortable-pageable-table/models';

export class RenewalTest {

  static generatePageData(page: number, size: number, total: number): PagedData<RenewalMember> {
    const content = this.generateList(size);
    return TestHelpers.generatePagedData(page, size, total, content);
  }
  static generateList(size: number): RenewalMember[] {
    const list: RenewalMember[] = [];

    for (let prefix = 0; prefix < size; prefix++) {
      list.push(this.generateRenewalMember(prefix));
    }
    return list;
  }

  static generateRenewalMember(prefix: number): RenewalMember {
    return {
      id: prefix,
      member_id: prefix,
      renewal_date: DateTime.now(),
      renewal_year: DateTime.now().year,
      full_name: {
      first_name: prefix + ' First',
        middle_name: prefix +  ' Middle',
        last_name: prefix + ' Last',
        suffix: prefix + ' Suffix'
      }
    }
  }

  static generateRenewal(): Renewal {
    return {
      id: 0,
      member_id: 0,
      renewal_date: DateTime.now(),
      renewal_year: DateTime.now().year,
    }
  }

  static generateForm(): FormModelGroup<Renewal> {
    return new FormGroup({
      id: new FormControl(0, {nonNullable: true}),
      member_id: new FormControl(0, {nonNullable: true}),
      renewal_date: new FormControl(DateTime.now(), {nonNullable: true}),
      renewal_year: new FormControl(DateTime.now().year, {nonNullable: true})
    })
  }

  static generateDialogInput(context: Renewal, action: EditAction)
    : EditDialogInput<Renewal, RenewalEditorComponent> {

    return {
      title: 'title',
      context: context,
      action: action,
      editorConfig: {
        component: RenewalEditorComponent,
        form: this.generateForm()
      }
    }
  }

  static generateColumnDefs(): ColumnDef<RenewalMember>[] {
    return [{
      columnName: 'renewal_date',
      displayName: 'renewals.list.columns.renewalDate',
      isSortable: true,
      cell: (element: RenewalMember) => `${element.renewal_date}`,
    }, {
      columnName: 'renewal_year',
      displayName: 'renewals.list.columns.renewalYear',
      isSortable: true,
      cell: (element: RenewalMember) => `${element.renewal_year}`
    }, {
      columnName: 'full_name',
      displayName: 'renewals.list.columns.fullName',
      isSortable: true,
      cell: (element: RenewalMember) => `${element.full_name}`
    }];
  }
}
