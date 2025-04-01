import {Component} from '@angular/core';

import {AuthService} from '@app/core/auth';

import {SortablePageableTableComponent} from '@app/shared/components/sortable-pageable-table';
import {BaseTableComponent} from '@app/shared/components/base-table-component';
import {EditAction, EditEvent} from '@app/shared/components/edit-dialog';

import {PhoneService} from '@app/features/phones/services/phone.service';
import {PhoneTableService} from '@app/features/phones/services/phone-table.service';
import {PhoneCreate, PhoneMember, Phone} from '@app/features/phones/models/phone';
import {PhoneEditDialogService} from '@app/features/phones/services/phone-edit-dialog.service';

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
export class ListPhonesComponent extends BaseTableComponent<PhoneCreate, Phone, PhoneMember>  {

  constructor(auth: AuthService,
              svc: PhoneService,
              tableSvc: PhoneTableService,
              dialogSvc: PhoneEditDialogService) {
    super(auth, svc, tableSvc, dialogSvc);
  }

  onEditClick(event: EditEvent<PhoneMember>) {
    const title = 'phones.list.dialog.update';
    const context = event.data as Phone;

    this.openEditDialog(this.dialogSvc.generateDialogInput(title, context, EditAction.Edit)).subscribe({
      next: result => console.log(result),
      error: err => this.errors.set(err)
    });
  }

  onDeleteClick(event: EditEvent<PhoneMember>) {
    const title = 'phones.list.dialog.delete';
    const context = event.data as Phone;

    this.openEditDialog(this.dialogSvc.generateDialogInput(title, context, EditAction.Delete)).subscribe({
      next: result => console.log(result),
      error: err => this.errors.set(err)
    });
  }
}
