import {Component, ViewEncapsulation} from '@angular/core';

import {AuthService} from '@app/core/auth';

import {SortablePageableTableComponent} from '@app/shared/components/sortable-pageable-table';
import {BaseTableComponent} from '@app/shared/components/base-table-component';
import {EditAction, EditEvent} from '@app/shared/components/edit-dialog';

import {
  Address,
  AddressCreate,
  AddressEditDialogService,
  AddressMember,
  AddressService,
  AddressTableService
} from '@app/features/addresses';

@Component({
  selector: 'app-list-addresses',
  imports: [SortablePageableTableComponent],
  providers: [AddressService, AddressTableService, AddressEditDialogService],
  templateUrl: './list-addresses.component.html',
  styleUrl: './list-addresses.component.scss',
  encapsulation: ViewEncapsulation.ShadowDom
})
export class ListAddressesComponent extends BaseTableComponent<AddressCreate, Address, AddressMember> {

  constructor(auth: AuthService,
              svc: AddressService,
              tableSvc: AddressTableService,
              dialogSvc: AddressEditDialogService,
              ) {
    super(auth, svc, tableSvc, dialogSvc );
  }

  onEditClick(event: EditEvent<AddressMember>): void {
    const title = 'addresses.list.dialog.update';
    const context = event.data as Address

    this.openEditDialog(this.dialogSvc.generateDialogInput(title, context, EditAction.Edit)).subscribe({
      next: result => console.log(result),
      error: err => this.errors.set(err)
    });
  }

  onDeleteClick(event: EditEvent<AddressMember>): void {
    const title = 'addresses.list.dialog.delete';
    const context = event.data as Address

    this.openEditDialog(this.dialogSvc.generateDialogInput(title, context, EditAction.Edit)).subscribe({
      next: result => console.log(result),
      error: err => this.errors.set(err)
    });
  }

}
