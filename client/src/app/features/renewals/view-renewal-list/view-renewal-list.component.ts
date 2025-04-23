import {Component, OnInit, input, signal} from '@angular/core';
import {BaseViewList} from '@app/shared/components/base-view-list/base-view-list';
import {Renewal} from '@app/features/renewals/models';
import {RenewalEditorComponent} from '@app/features/renewals/renewal-editor/renewal-editor.component';
import {RenewalService} from '@app/features/renewals/services/renewal.service';
import {AuthService} from '@app/core/auth/services/auth.service';
import {RenewalEditDialogService} from '@app/features/renewals/services/renewal-edit-dialog.service';
import {PermissionType} from '@app/core/auth/models/permission-type';
import {DateTime} from 'luxon';
import {EditAction} from '@app/shared/components/base-edit-dialog/models';
import {ViewModelListComponent} from '@app/shared/components/view-model-list/view-model-list.component';

@Component({
  selector: 'app-view-renewal-list',
  imports: [
    ViewModelListComponent
  ],
  templateUrl: './view-renewal-list.component.html',
  styleUrl: './view-renewal-list.component.scss'
})
export class ViewRenewalListComponent extends BaseViewList<Renewal, RenewalEditorComponent> implements  OnInit {
  memberId = input.required<number>();
  hasWritePrivileges = signal(false);

  private renewalSvc: RenewalService;

  constructor(private auth: AuthService,
              apiSvc: RenewalService,
              dialogSvc: RenewalEditDialogService) {
    super(apiSvc, dialogSvc);
    this.renewalSvc = apiSvc;
  }

  ngOnInit(): void {
    this.hasWritePrivileges.update(() => this.auth.hasPermission(PermissionType.write));

    this.renewalSvc.getAllByMember(this.memberId()).subscribe({
      next: list => this.items.update(() => list),
      error: err => console.debug(err) // TODO: better error handling
    })
  }

  public onAddItem(): void {
    const title = 'editDialog.renewal.create';
    const context = this.generateEmptyRenewal();
    this.processAction(title, context, EditAction.Create);
  }

  public onEditItem(item: Renewal): void {
    const title = 'editDialog,renewal.edit';
    this.processAction(title, item, EditAction.Edit);
  }

  public onDeleteItem(item: Renewal): void {
    const title = 'editDialog.renewal.delete';
    this.processAction(title, item, EditAction.Delete);
  }

  private generateEmptyRenewal(): Renewal {
    return {
      id: 0,
      member_id: this.memberId(),
      renewal_year: 0,
      renewal_date: DateTime.now()
    }
  }
}
