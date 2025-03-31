import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {first, merge, mergeMap, Observable} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";

import {AuthService} from '@app/core/auth';

import {SortablePageableTableComponent} from "@app/shared/components/sortable-pageable-table";
import {BaseTableComponent} from "@app/shared/components/base-table-component";
import {EditAction, EditDialogResult, EditEvent} from '@app/shared/components/edit-dialog';
import {PagedData} from '@app/shared/services/api-service';

import {
  PhoneEditDialogService, PhoneService, PhoneTableService, Phone, PhoneMember, PhoneCreate
} from '@app/features/phones';

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
