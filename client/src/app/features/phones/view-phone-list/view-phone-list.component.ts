import {Component, OnInit, input, signal} from '@angular/core';
import {BaseViewList} from '@app/shared/components/base-view-list/base-view-list';
import {Phone, PhoneType} from '@app/features/phones/models';
import {PhoneEditorComponent} from '@app/features/phones/phone-editor/phone-editor.component';
import {PhoneService} from '@app/features/phones/services/phone.service';
import {AuthService} from '@app/core/auth/services/auth.service';
import {PhoneEditDialogService} from '@app/features/phones/services/phone-edit-dialog.service';
import {PermissionType} from '@app/core/auth/models/permission-type';
import {ViewModelListComponent} from '@app/shared/components/view-model-list/view-model-list.component';
import {EditAction} from '@app/shared/components/base-edit-dialog/models';

@Component({
  selector: 'app-view-phone-list',
  imports: [
    ViewModelListComponent
  ],
  templateUrl: './view-phone-list.component.html',
  styleUrl: './view-phone-list.component.scss'
})
export class ViewPhoneListComponent extends BaseViewList<Phone, PhoneEditorComponent> implements OnInit {
  memberId = input.required<number>();
  hasWritePrivileges = signal(false);

  private phoneSvc: PhoneService

  constructor(private auth: AuthService,
              apiSvc: PhoneService,
              dialogSvc: PhoneEditDialogService) {
    super(apiSvc, dialogSvc);
    this.phoneSvc = apiSvc
  }

  ngOnInit(): void {
    this.hasWritePrivileges.update(() => this.auth.hasPermission(PermissionType.write));

    this.phoneSvc.getAllByMember(this.memberId()).subscribe({
      next: list => this.items.update(() => list),
      error: err => console.debug(err) // TODO: better error handling
    })
  }

  onAddItem(): void {
    const title = 'editDialog.phone.create';
    const context = this.generateEmptyPhone();
    this.processAction(title, context, EditAction.Create);
  }

  onEditItem(item: Phone): void {
    const title = 'editDialog,phone.edit';
    this.processAction(title, item, EditAction.Edit);
  }

  onDeleteItem(item: Phone): void {
    const title = 'editDialog.phone.delete';
    this.processAction(title, item, EditAction.Delete);
  }

  private generateEmptyPhone(): Phone {
    return {
      id: 0,
      member_id: this.memberId(),
      country_code: '',
      phone_number: '',
      phone_type: PhoneType.Home
    }
  }
}
