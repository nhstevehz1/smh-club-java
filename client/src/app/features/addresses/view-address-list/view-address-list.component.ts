import {Component, input, signal, OnInit} from '@angular/core';
import {AuthService} from '@app/core/auth/services/auth.service';
import {PermissionType} from '@app/core/auth/models/permission-type';
import {ViewModelListComponent} from '@app/shared/components/view-model-list/view-model-list.component';
import {Address, AddressType} from '@app/features/addresses/models';
import {AddressEditDialogService} from '@app/features/addresses/services/address-edit-dialog.service';
import {EditAction} from '@app/shared/components/base-edit-dialog/models';
import {AddressService} from '@app/features/addresses/services/address.service';
import {BaseViewList} from '@app/shared/components/base-view-list/base-view-list';
import {AddressEditorComponent} from '@app/features/addresses/address-editor/address-editor.component';

@Component({
  selector: 'app-view-address-list',
  imports: [
    ViewModelListComponent
  ],
  templateUrl: './view-address-list.component.html',
  styleUrl: './view-address-list.component.scss'
})
export class ViewAddressListComponent extends BaseViewList<Address, AddressEditorComponent> implements OnInit{
  memberId = input.required<number>();
  hasWritePrivileges = signal(false);

  private addressSvc: AddressService

  constructor(private auth: AuthService,
              apiSvc: AddressService,
              dialogSvc: AddressEditDialogService) {
    super(apiSvc, dialogSvc);
    this.addressSvc = apiSvc;
  }

  ngOnInit(): void {
    this.hasWritePrivileges.update(() => this.auth.hasPermission(PermissionType.write));

    this.addressSvc.getAllByMember(this.memberId()).subscribe({
      next: list => this.items.update(() => list),
      error: err => console.debug(err) // TODO: better error handling
    })
  }

  onAddItem(): void {
    const title = 'editDialog.address.create';
    const context = this.generateEmptyAddress();
    this.processAction(title, context, EditAction.Create);
  }

  onEditItem(item: Address) {
    const title = 'editDialog,address.edit';
    this.processAction(title, item, EditAction.Edit);
  }

  onDeleteItem(item: Address) {
    const title = 'editDialog.address.delete';
    this.processAction(title, item, EditAction.Delete);
  }

  private generateEmptyAddress(): Address {
    return {
      id: 0,
      member_id: this.memberId(),
      address1: '',
      address2: '',
      city: '',
      state: '',
      postal_code: '',
      address_type: AddressType.Other
    }
  }
}
