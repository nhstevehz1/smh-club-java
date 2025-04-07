import {Component, ViewEncapsulation} from '@angular/core';

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
import {mergeMap, map} from 'rxjs/operators';
import {of} from 'rxjs';

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
      mergeMap(result => {
        if(result.action != EditAction.Cancel) {
          return this.apiSvc.update(result.context).pipe(
            map(() => result)
          );
        } else {
          return of(result);
        }
      })
    ).subscribe({
      next: result => {
        if(result.action == EditAction.Edit) {
          const update: AddressMember = {
            id: result.context.id,
            member_id: result.context.member_id,
            address1: result.context.address1,
            address2: result.context.address2,
            city: result.context.city,
            state: result.context.state,
            postal_code: result.context.postal_code,
            address_type: result.context.address_type,
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
      mergeMap(result => {
        if(result.action == EditAction.Delete) {
          return this.apiSvc.delete(result.context.id).pipe(
            map(() => result)
          );
        } else {
          return of(result)
        }
      })
    ).subscribe({
      next: result=> {
        if(result.action == EditAction.Delete) {
          this.deleteItem(event.idx);
        }
    },
      error: err => this.errors.set(err)
    });
  }


}
