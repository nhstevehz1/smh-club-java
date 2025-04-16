import {Component, ViewChild} from '@angular/core';
import {mergeMap, map} from 'rxjs/operators';
import {of} from 'rxjs';

import {AuthService} from '@app/core/auth/services/auth.service';
import {
  SortablePageableTableComponent
} from '@app/shared/components/sortable-pageable-table/sortable-pageable-table.component';
import {BaseTableComponent} from '@app/shared/components/base-table-component/base-table.component';
import {EditAction, EditEvent} from '@app/shared/components/base-edit-dialog/models';

import {RenewalMember, Renewal} from '@app/features/renewals/models/renewal';
import {RenewalService} from '@app/features/renewals/services/renewal.service';
import {RenewalTableService} from '@app/features/renewals/services/renewal-table.service';
import {RenewalEditDialogService} from '@app/features/renewals/services/renewal-edit-dialog.service';
import {RenewalEditorComponent} from '@app/features/renewals/renewal-editor/renewal-editor.component';

@Component({
  selector: 'app-list-renewals',
  imports: [SortablePageableTableComponent],
  providers: [RenewalService, AuthService],
  templateUrl: './list-renewals.component.html',
  styleUrl: './list-renewals.component.scss'
})
export class ListRenewalsComponent
  extends BaseTableComponent<Renewal, RenewalMember, RenewalEditorComponent>  {

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
    const context = event.data as Renewal;
    const dialogInput = this.dialogSvc.generateDialogInput(title, context, EditAction.Edit)

    this.openEditDialog(dialogInput).pipe(
      mergeMap(renewalResult => {
        if(renewalResult.action == EditAction.Edit) {
          return this.apiSvc.update(renewalResult.context);
        } else {
          return of(null);
        }
      })
    ).subscribe({
      next: renewalResult => {
        if(renewalResult) {
          const update: RenewalMember = {
            id: renewalResult.id,
            member_id: renewalResult.member_id,
            renewal_year: renewalResult.renewal_year,
            renewal_date: renewalResult.renewal_date,
            full_name: event.data.full_name
          }
          this.updateItem(update);
        }

      },
      error: err => this.errors.set(err)
    });
  }

  onDeleteClick(event: EditEvent<RenewalMember>): void {
    const title = 'renewals.list.dialog.delete';
    const context = event.data as Renewal
    const dialogInput = this.dialogSvc.generateDialogInput(title, context, EditAction.Delete)

    this.openEditDialog(dialogInput).pipe(
      mergeMap(renewalResult => {
        if(renewalResult.action == EditAction.Delete) {
          return this.apiSvc.delete(renewalResult.context.id).pipe(
            map(() => renewalResult)
          );
        } else {
          return of(renewalResult);
        }
      })
    ).subscribe({
      next: renewalResult => {
        if (renewalResult.action == EditAction.Delete) {
          this.deleteItem(event.idx);
        }
      },
      error: err => this.errors.set(err)
    });
  }
}
