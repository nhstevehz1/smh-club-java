import {Component, ViewEncapsulation} from '@angular/core';
import {mergeMap, map} from 'rxjs/operators';
import {of} from 'rxjs';

import {AuthService} from '@app/core/auth/services/auth.service';
import {
  SortablePageableTableComponent
} from '@app/shared/components/sortable-pageable-table/sortable-pageable-table.component';
import {BaseTableComponent} from '@app/shared/components/base-table-component/base-table.component';
import {EditAction, EditEvent} from '@app/shared/components/base-edit-dialog/models';

import {AddressCreate, AddressMember, Address} from '@app/features/addresses/models/address';
import {AddressService} from '@app/features/addresses/services/address.service';
import {AddressTableService} from '@app/features/addresses/services/address-table.service';
import {AddressEditDialogService} from '@app/features/addresses/services/address-edit-dialog.service';
import {AddressEditorComponent} from '@app/features/addresses/address-editor/address-editor.component';


@Component({
  selector: 'app-list-addresses',
  imports: [SortablePageableTableComponent],
  providers: [AddressService, AddressTableService, AddressEditDialogService],
  templateUrl: './list-addresses.component.html',
  styleUrl: './list-addresses.component.scss',
  encapsulation: ViewEncapsulation.ShadowDom
})
export class ListAddressesComponent
  extends BaseTableComponent<AddressCreate, Address, AddressMember, AddressEditorComponent> {

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
    const dialogInput = this.dialogSvc.generateDialogInput(title, context, EditAction.Edit);

    this.openEditDialog(dialogInput).pipe(
      mergeMap(addressResult => {
        if(addressResult.action == EditAction.Edit) {
          return this.apiSvc.update(addressResult.context);
        } else {
          return of(null);
        }
      })
    ).subscribe({
      next: addressResult => {
        if(addressResult) {
          const update: AddressMember = {
            id: addressResult.id,
            member_id: addressResult.member_id,
            address1: addressResult.address1,
            address2: addressResult.address2,
            city: addressResult.city,
            state: addressResult.state,
            postal_code: addressResult.postal_code,
            address_type: addressResult.address_type,
            full_name: event.data.full_name
          }
          this.updateItem(update)
        }
      },
      error: err => this.errors.set(err)
    })
  }

  onDeleteClick(event: EditEvent<AddressMember>): void {
    const title = 'addresses.list.dialog.delete';
    const context = event.data as Address
    const dialogInput = this.dialogSvc.generateDialogInput(title, context, EditAction.Delete);

    this.openEditDialog(dialogInput).pipe(
      mergeMap(addressResult => {
        if(addressResult.action == EditAction.Delete) {
          return this.apiSvc.delete(addressResult.context.id).pipe(
            map(() => addressResult)
          );
        } else {
          return of(addressResult)
        }
      })
    ).subscribe({
      next: addressResult=> {
        if(addressResult.action == EditAction.Delete) {
          this.deleteItem(event.idx);
        }
    },
      error: err => this.errors.set(err)
    });
  }


}
