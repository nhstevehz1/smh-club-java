import {Component} from '@angular/core';

import {AuthService} from '@app/core/auth/services/auth.service';

import {
  SortablePageableTableComponent
} from '@app/shared/components/sortable-pageable-table/sortable-pageable-table.component';
import {BaseTableComponent} from '@app/shared/components/base-table-component/base-table.component';
import {EditAction, EditEvent} from '@app/shared/components/base-edit-dialog/models';

import {PhoneService} from '@app/features/phones/services/phone.service';
import {PhoneTableService} from '@app/features/phones/services/phone-table.service';
import {PhoneMember, Phone} from '@app/features/phones/models/phone';
import {PhoneEditDialogService} from '@app/features/phones/services/phone-edit-dialog.service';
import {PhoneEditorComponent} from '@app/features/phones/phone-editor/phone-editor.component';
import {mergeMap, of} from 'rxjs';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-list-phones',
  imports: [SortablePageableTableComponent],
  providers: [
    PhoneService,
    AuthService,
    PhoneTableService,
    PhoneEditDialogService
  ],
  templateUrl: './list-phones.component.html',
  styleUrl: './list-phones.component.scss'
})
export class ListPhonesComponent extends BaseTableComponent<Phone, PhoneMember, PhoneEditorComponent>  {

  constructor(auth: AuthService,
              svc: PhoneService,
              tableSvc: PhoneTableService,
              dialogSvc: PhoneEditDialogService) {
    super(auth, svc, tableSvc, dialogSvc);
  }

  onEditClick(event: EditEvent<PhoneMember>) {
    const title = 'phones.list.dialog.update';
    const context = event.data as Phone;
    const dialogInput = this.dialogSvc.generateDialogInput(title, context, EditAction.Edit)

    this.openEditDialog(dialogInput).pipe(
      mergeMap(phoneResult => {
        if(phoneResult.action == EditAction.Edit) {
          return this.apiSvc.update(phoneResult.context);
        } else {
          return of(null);
        }
      })
    ).subscribe({
      next: phoneResult => {
        if(phoneResult) {
          const update: PhoneMember = {
            id: phoneResult.id,
            member_id: phoneResult.member_id,
            country_code: phoneResult.country_code,
            phone_number: phoneResult.phone_number,
            phone_type: phoneResult.phone_type,
            full_name: event.data.full_name
          }
          this.updateItem(update);
        }
      },
      error: err => this.errors.set(err)
    });
  }

  onDeleteClick(event: EditEvent<PhoneMember>) {
    const title = 'phones.list.dialog.delete';
    const context = event.data as Phone;
    const dialogInput = this.dialogSvc.generateDialogInput(title, context, EditAction.Delete);

    this.openEditDialog(dialogInput).pipe(
      mergeMap(phoneResult => {
        if(phoneResult.action == EditAction.Delete) {
          return this.apiSvc.delete(phoneResult.context.id).pipe(
            map(() => phoneResult)
          );
        } else {
          return of(phoneResult);
        }
      })
    ).subscribe({
      next: phoneResult => {
        if(phoneResult.action == EditAction.Delete) {
          this.deleteItem(event.idx)
        }
      },
      error: err => this.errors.set(err)
    });
  }
}
