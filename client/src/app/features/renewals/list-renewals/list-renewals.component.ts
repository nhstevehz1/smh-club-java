import {Component, ViewChild} from '@angular/core';

import {AuthService} from '@app/core/auth';
import {SortablePageableTableComponent} from '@app/shared/components/sortable-pageable-table';
import {BaseTableComponent} from '@app/shared/components/base-table-component';
import {EditAction, EditEvent} from '@app/shared/components/edit-dialog';

import {RenewalCreate, RenewalMember, Renewal} from '@app/features/renewals/models/renewal';
import {RenewalService} from '@app/features/renewals/services/renewal.service';
import {RenewalTableService} from '@app/features/renewals/services/renewal-table.service';
import {RenewalEditDialogService} from '@app/features/renewals/services/renewal-edit-dialog.service';

@Component({
  selector: 'app-list-renewals',
  imports: [SortablePageableTableComponent],
  providers: [RenewalService, AuthService],
  templateUrl: './list-renewals.component.html',
  styleUrl: './list-renewals.component.scss'
})
export class ListRenewalsComponent extends BaseTableComponent<RenewalCreate, Renewal, RenewalMember>  {

  @ViewChild(SortablePageableTableComponent, {static: true})
  private _table!: SortablePageableTableComponent<RenewalMember>;

  constructor(auth: AuthService,
              svc: RenewalService,
              tableSvc: RenewalTableService,
              dialogSvc: RenewalEditDialogService) {
    super(auth, svc, tableSvc, dialogSvc);
  }


  onEditClick(event: EditEvent<RenewalMember>): void {
    const title = 'renewals.list.dialog.update';
    const context = event.data as Renewal

    this.openEditDialog(this.dialogSvc.generateDialogInput(title, context, EditAction.Edit)).subscribe({
      next: result => console.log(result),
      error: err => this.errors.set(err)
    });
  }

  onDeleteClick(event: EditEvent<RenewalMember>): void {
    const title = 'renewals.list.dialog.delete';
    const context = event.data as Renewal

    this.openEditDialog(this.dialogSvc.generateDialogInput(title, context, EditAction.Delete)).subscribe({
      next: result => console.log(result),
      error: err => this.errors.set(err)
    });
  }
}
